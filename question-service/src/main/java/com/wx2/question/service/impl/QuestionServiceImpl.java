package com.wx2.question.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.common.error.QuestionError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.PageData;
import com.wx2.question.mapper.QuestionMapper;
import com.wx2.question.model.entity.Knowledge;
import com.wx2.question.model.entity.Question;
import com.wx2.question.model.entity.Relation;
import com.wx2.question.model.es.QuestionEsDoc;
import com.wx2.question.model.query.QuestionHotRankQuery;
import com.wx2.question.model.query.QuestionPageQuery;
import com.wx2.question.model.query.QuestionQuery;
import com.wx2.question.model.vo.QuestionHotRankVO;
import com.wx2.question.model.vo.QuestionVO;
import com.wx2.question.service.KnowledgeService;
import com.wx2.question.service.RelationService;
import com.wx2.question.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wx2.common.constant.MqConstant.*;
import static com.wx2.common.constant.RedisConstant.*;

@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private KnowledgeService knowledgeService;
    @Lazy
    @Autowired
    private RelationService relationService;
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public QuestionVO getQuestionById(Long questionId, String ip) {
        // 对应缓存穿透
        // QuestionVO questionVO = getWithPassThrough(questionId);
        // 对应缓存击穿
        QuestionVO questionVO = getWithMutex(questionId);
        if (ObjectUtil.isNull(questionVO)) {
            throw new BizException(QuestionError.QUESTION_NOT_EXIST);
        }
        // 增加题目热度值
        questionHotIncrease(questionId, ip, questionVO.getSubject(), questionVO.getContent());

        return questionVO;
    }

    @Override
    public PageData<QuestionVO> getQuestionByPage(QuestionPageQuery query) {
        try {
            // 通过ES分页查询题目
            return getByEs(query);
        } catch (Exception e) {
            throw new RuntimeException("ES查询出现异常", e);
        }
    }

    @Override
    @Transactional
    public void addQuestion(QuestionQuery query) {
        // 新增题目
        Question question = BeanUtil.copyProperties(query, Question.class);
        save(question);
        // 发送ES同步更新消息
        sendUpdateMessage(question.getId());
    }

    @Override
    @Transactional
    public void updateQuestion(QuestionQuery query) {
        // 检验题目是否存在
        Question question = getById(query.getId());
        if (ObjectUtil.isNull(question)) {
            throw new BizException(QuestionError.QUESTION_NOT_EXIST);
        }
        // 更新题目
        updateById(BeanUtil.copyProperties(query, Question.class));
        // 将题目从redis中删除
        stringRedisTemplate.delete(CACHE_QUESTION_PREFIX + question.getId());
        // 发送ES同步更新消息
        sendUpdateMessage(query.getId());
    }

    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        // 删除题目及对应关联
        removeById(questionId);
        relationService.remove(new LambdaQueryWrapper<Relation>()
                .eq(Relation::getQuestionId, questionId));

        // 发送ES同步更新消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_QUESTION_DELETE_KEY, questionId.toString());
        } catch (Exception e) {
            log.error("发送ES题目同步删除消息失败，id：{}", questionId, e);
        }
    }

    @Override
    public void sendUpdateMessage(Long questionId) {
        // 查询题目
        Question question = getById(questionId);
        // 查询题目关联
        List<Relation> relationList = relationService.list(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getQuestionId, questionId)
        );
        // 查询对应的知识点名
        List<String> knowledgeNames = getKnowledgeNames(relationList);
        // 封装成EsDoc对应ES中字段
        QuestionEsDoc questionEsDoc = new QuestionEsDoc();
        BeanUtil.copyProperties(question, questionEsDoc);
        questionEsDoc.setKnowledgeList(knowledgeNames);
        // 向消息队列发送ES同步更新消息
        try {
            rabbitTemplate.convertAndSend(ES_EXCHANGE, ES_QUESTION_UPDATE_KEY, questionEsDoc);
        } catch (Exception e) {
            log.error("发送ES题目同步更新消息失败，id：{}", questionId, e);
        }
    }

    @Override
    public List<QuestionHotRankVO> getQuestionHotRank(QuestionHotRankQuery request) {
        Integer topN = request.getTopN();
        Integer subject = request.getSubject();
        // 根据学科类型查询对应的热榜数据
        Set<ZSetOperations.TypedTuple<String>> hotRankTuples = switch (subject) {
            case 1 -> stringRedisTemplate.opsForZSet()
                    .reverseRangeWithScores(MATH_HOT_RANK_PREFIX, 0, topN - 1);
            case 2 -> stringRedisTemplate.opsForZSet()
                    .reverseRangeWithScores(ENGLISH_HOT_RANK_PREFIX, 0, topN - 1);
            case 3 -> stringRedisTemplate.opsForZSet()
                    .reverseRangeWithScores(POLITICS_HOT_RANK_PREFIX, 0, topN - 1);
            default -> null;
        };
        // 没有数据返回空列表
        if (CollectionUtil.isEmpty(hotRankTuples)) {
            return List.of();
        }
        // 将查询结果转换为VO列表
        List<QuestionHotRankVO> hotRankList = new ArrayList<>(hotRankTuples.size());
        int rank = 1;

        for (ZSetOperations.TypedTuple<String> hotRankTuple : hotRankTuples) {
            String value = hotRankTuple.getValue();
            Double score = hotRankTuple.getScore();
            if (StrUtil.isBlank(value)) {
                continue;
            }
            // parts[0]为题目id，parts[1]为题目内容
            String[] parts = value.split(":", 2);
            // 封装成VO对象
            QuestionHotRankVO vo = new QuestionHotRankVO();
            vo.setRank(rank++);
            vo.setQuestionId(Long.parseLong(parts[0]));
            vo.setContent(parts[1]);
            vo.setHotScore(score.longValue());
            hotRankList.add(vo);
        }

        return hotRankList;
    }

    private List<String> getKnowledgeNames(List<Relation> relationList) {
        if (CollectionUtil.isEmpty(relationList)) {
            return List.of();
        }
        // 解析出知识点id列表
        List<Long> knowledgeIds = relationList.stream()
                .map(Relation::getKnowledgeId)
                .collect(Collectors.toList());
        // 查询知识点名称
        List<Knowledge> knowledgeList = knowledgeService.listByIds(knowledgeIds);

        return knowledgeList.stream()
                .map(Knowledge::getName)
                .collect(Collectors.toList());
    }

    private PageData<QuestionVO> getByEs(QuestionPageQuery query) throws IOException {
        // 创建ES查询请求构建器
        SearchRequest.Builder requestBuilder = new SearchRequest.Builder().index("question");
        // 创建ES查询请求构建器
        requestBuilder.query(q -> q.bool(b -> {
            // 关键词匹配
            if (StrUtil.isNotBlank(query.getContent())) {
                b.should(s -> s.match(m -> m.field("content").query(query.getContent())));
                b.minimumShouldMatch("1");
            }
            // 难度筛选
            if (ObjectUtil.isNotNull(query.getDifficulty())) {
                b.filter(f -> f.term(t -> t.field("difficulty").value(query.getDifficulty())));
            }
            // 学科筛选
            if (ObjectUtil.isNotNull(query.getSubject())) {
                b.filter(f -> f.term(t -> t.field("subject").value(query.getSubject())));
            }
            // 类型筛选
            if (ObjectUtil.isNotNull(query.getType())) {
                b.filter(f -> f.term(t -> t.field("type").value(query.getType())));
            }
            // 真题筛选
            if (ObjectUtil.isNotNull(query.getIsReal())) {
                b.filter(f -> f.term(t -> t.field("is_real").value(query.getIsReal())));
            }
            // 知识点筛选
            if (StrUtil.isNotBlank(query.getKnowledgePoint())) {
                b.filter(f -> f.term(t -> t.field("knowledgeList.keyword").value(query.getKnowledgePoint())));
            }

            return b;

        }));
        // 构建排序条件
        if (StrUtil.isNotBlank(query.getSortField())) {
            SortOrder order = "asc".equalsIgnoreCase(query.getSortDirection())
                    ? SortOrder.Asc
                    : SortOrder.Desc;
            requestBuilder.sort(s -> s.field(f -> f.field(query.getSortField()).order(order)));
        } else {
            requestBuilder.sort(s -> s.score(sc -> sc.order(SortOrder.Desc)));
        }
        // 构建分页条件
        requestBuilder.from(query.getOffset()).size(query.getSize());
        // 执行ES查询
        SearchResponse<QuestionEsDoc> response = elasticsearchClient.search(
                requestBuilder.build(),
                QuestionEsDoc.class
        );
        // 将查询结果封装成为VO列表
        List<QuestionVO> questionVOList = response.hits().hits().stream()
                .map(hit -> {
                    QuestionEsDoc question = hit.source();
                    QuestionVO vo = new QuestionVO();
                    BeanUtil.copyProperties(question, vo);
                    return vo;
                })
                .collect(Collectors.toList());
        // 计算总记录数
        long total = 0;
        TotalHits totalHits = response.hits().total();
        if (totalHits != null && totalHits.relation() == TotalHitsRelation.Eq) {
            total = totalHits.value();
        }
        // 构建并返回分页数据对象
        return new PageData<>(questionVOList, total, query);
    }

    private QuestionVO getWithPassThrough(Long questionId) {
        String cacheKey = CACHE_QUESTION_PREFIX + questionId;
        // 从redis查询题目
        String questionJson = stringRedisTemplate.opsForValue().get(cacheKey);
        // 缓存命中直接返回
        if (StrUtil.isNotBlank(questionJson)) {
            return JSONUtil.toBean(questionJson, QuestionVO.class);
        }
        if (questionJson != null) {
            return null;
        }
        // 从数据库查询题目
        Question question = getById(questionId);
        // 题目不存在，缓存空值
        if (ObjectUtil.isNull(question)) {
            stringRedisTemplate.opsForValue().set(cacheKey, "", NULL_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            return null;
        }
        // 查询并设置对应知识点列表字段
        List<Relation> relationList = relationService.list(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getQuestionId, questionId)
        );
        List<String> knowledgeNames = getKnowledgeNames(relationList);
        QuestionVO questionVO = new QuestionVO();
        BeanUtil.copyProperties(question, questionVO);
        questionVO.setKnowledgeList(knowledgeNames);
        // 存入redis
        stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(questionVO), EXPIRATION_MINUTES, TimeUnit.MINUTES);

        return questionVO;
    }
    private QuestionVO getWithMutex(Long questionId) {
        String cacheKey = CACHE_QUESTION_PREFIX + questionId;
        // 从redis查询题目
        String questionJson = stringRedisTemplate.opsForValue().get(cacheKey);
        // 缓存命中直接返回
        if (StrUtil.isNotBlank(questionJson)) {
            return JSONUtil.toBean(questionJson, QuestionVO.class);
        }
        if (questionJson != null) {
            return null;
        }
        // 重建缓存
        String lockKey = QUESTION_LOCK_PREFIX + questionId;
        QuestionVO questionVO = new QuestionVO();

        try {
            // 尝试获取互斥锁
            boolean isLock = tryLock(lockKey);
            // 未获取到锁，50m后重试
            if (!isLock) {
                Thread.sleep(50);
                return getWithMutex(questionId);
            }

            // 从数据库查询题目
            Question question = getById(questionId);
            // 题目不存在，缓存空值
            if (ObjectUtil.isNull(question)) {
                stringRedisTemplate.opsForValue().set(cacheKey, "", NULL_EXPIRATION_MINUTES, TimeUnit.MINUTES);
                return null;
            }
            // 查询并设置对应知识点列表字段
            List<Relation> relationList = relationService.list(
                    new LambdaQueryWrapper<Relation>()
                            .eq(Relation::getQuestionId, questionId)
            );
            List<String> knowledgeNames = getKnowledgeNames(relationList);
            BeanUtil.copyProperties(question, questionVO);
            questionVO.setKnowledgeList(knowledgeNames);

            // 存入redis
            stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(questionVO), EXPIRATION_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放互斥锁
            unlock(lockKey);
        }

        return questionVO;
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    private void questionHotIncrease(Long questionId, String ip, Integer subject, String content) {
        // 构建ip点击记录的缓存键
        String ipClickKey = QUESTION_CLICK_IP_PREFIX + questionId + ":" + ip;
        // 尝试设置ip点击记录
        Boolean isValidClick = stringRedisTemplate.opsForValue()
                .setIfAbsent(ipClickKey, "1", CLICK_COOLDOWN_SECONDS, TimeUnit.SECONDS);
        // 如果是有效点击，则根据学科类型更新对应的热度值
        if (Boolean.TRUE.equals(isValidClick)) {
            // 截取题目内容，生成简短内容
            String contentSummary = truncateContent(content);
            String zSetValue = questionId + ":" + contentSummary;
            switch (subject) {
                case 1:
                    stringRedisTemplate.opsForZSet()
                            .incrementScore(MATH_HOT_RANK_PREFIX, zSetValue, 1);
                    break;
                case 2:
                    stringRedisTemplate.opsForZSet()
                            .incrementScore(ENGLISH_HOT_RANK_PREFIX, zSetValue, 1);
                    break;
                case 3:
                    stringRedisTemplate.opsForZSet()
                            .incrementScore(POLITICS_HOT_RANK_PREFIX, zSetValue, 1);
                    break;
                default:
                    break;
            }

        }
    }

    private String truncateContent(String content) {
        if (content.length() <= 50) {
            return content;
        }
        return content.substring(0, 50) + "...";
    }
}
