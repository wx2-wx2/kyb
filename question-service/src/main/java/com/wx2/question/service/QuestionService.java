package com.wx2.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.common.model.PageData;
import com.wx2.question.model.entity.Question;
import com.wx2.question.model.query.QuestionHotRankQuery;
import com.wx2.question.model.query.QuestionPageQuery;
import com.wx2.question.model.query.QuestionQuery;
import com.wx2.question.model.vo.QuestionHotRankVO;
import com.wx2.question.model.vo.QuestionVO;

import java.util.List;

public interface QuestionService extends IService<Question> {
    /**
     * 根据题目id查询题目
     */
    QuestionVO getQuestionById(Long questionId, String ip);

    /**
     * 分页查询题目
     */
    PageData<QuestionVO> getQuestionByPage(QuestionPageQuery query);

    /**
     * 新增题目
     */
    void addQuestion(QuestionQuery query);

    /**
     * 更新题目
     */
    void updateQuestion(QuestionQuery query);

    /**
     * 删除题目
     */
    void deleteQuestion(Long questionId);

    /**
     * 向消息队列发送ES同步更新消息
     */
    void sendUpdateMessage(Long questionId);

    /**
     * 获取实时热度排行榜
     */
    List<QuestionHotRankVO> getQuestionHotRank(QuestionHotRankQuery query);
}
