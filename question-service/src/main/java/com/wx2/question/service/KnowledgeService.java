package com.wx2.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.question.model.entity.Knowledge;
import com.wx2.question.model.query.KnowledgeQuery;
import com.wx2.question.model.vo.KnowledgeVO;

import java.util.List;

public interface KnowledgeService extends IService<Knowledge> {
    /**
     * 根据学科查询知识点
     */
    List<KnowledgeVO> getKnowledgeBySubject(Long subjectId);

    /**
     * 新增知识点
     */
    void addKnowledge(KnowledgeQuery query);

    /**
     * 更新知识点
     */
    void updateKnowledge(KnowledgeQuery query);

    /**
     * 删除知识点
     */
    void deleteKnowledge(Long knowledgeId);
}
