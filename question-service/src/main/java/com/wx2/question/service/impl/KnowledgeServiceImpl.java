package com.wx2.question.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.common.error.QuestionError;
import com.wx2.common.exception.BizException;
import com.wx2.question.mapper.KnowledgeMapper;
import com.wx2.question.model.entity.Knowledge;
import com.wx2.question.model.query.KnowledgeQuery;
import com.wx2.question.model.vo.KnowledgeVO;
import com.wx2.question.service.KnowledgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {

    @Override
    public List<KnowledgeVO> getKnowledgeBySubject(Long subjectId) {
        // 批量查询知识点
        List<Knowledge> knowledgeList = list(
                new LambdaQueryWrapper<Knowledge>()
                        .eq(Knowledge::getSubject, subjectId)
        );

        return knowledgeList.stream()
                .map(knowledge -> {
                    KnowledgeVO vo = new KnowledgeVO();
                    BeanUtil.copyProperties(knowledge, vo);
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addKnowledge(KnowledgeQuery query) {
        // 检验知识点是否已存在
        boolean exists = lambdaQuery()
                .eq(Knowledge::getName, query.getName())
                .eq(Knowledge::getSubject, query.getSubject())
                .exists();
        if (exists) {
            throw new BizException(QuestionError.KNOWLEDGE_ALREADY_EXIST);
        }
        // 新增知识点
        Knowledge knowledge = new Knowledge();
        BeanUtil.copyProperties(query, knowledge);
        save(knowledge);
    }

    @Override
    @Transactional
    public void updateKnowledge(KnowledgeQuery query) {
        // 检验知识点是否存在
        Knowledge knowledge = getById(query.getId());
        if (ObjectUtil.isNull(knowledge)) {
            throw new BizException(QuestionError.KNOWLEDGE_NOT_EXIST);
        }
        // 检验是否有重复知识点
        boolean exists = lambdaQuery()
                .eq(Knowledge::getName, query.getName())
                .eq(Knowledge::getSubject, query.getSubject())
                .ne(Knowledge::getId, query.getId())
                .exists();
        if (exists) {
            throw new BizException(QuestionError.KNOWLEDGE_ALREADY_EXIST);
        }
        // 更新知识点
        knowledge.setName(query.getName());
        knowledge.setSubject(query.getSubject());
        updateById(knowledge);
    }

    @Override
    @Transactional
    public void deleteKnowledge(Long knowledgeId) {
        removeById(knowledgeId);
    }
}
