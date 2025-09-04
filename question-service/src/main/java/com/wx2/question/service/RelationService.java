package com.wx2.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.question.model.entity.Relation;
import com.wx2.question.model.query.RelationQuery;

public interface RelationService extends IService<Relation> {
    /**
     * 给题目关联指定知识点
     */
    void addRelation(RelationQuery query);

    /**
     * 解除关联
     */
    void deleteRelation(RelationQuery query);
}
