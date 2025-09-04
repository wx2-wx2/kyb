package com.wx2.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.question.mapper.RelationMapper;
import com.wx2.question.model.entity.Relation;
import com.wx2.question.model.query.RelationQuery;
import com.wx2.question.service.RelationService;
import com.wx2.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService {

    @Lazy
    @Autowired
    private QuestionService questionService;

    @Override
    public void addRelation(RelationQuery query) {
        // 新增关联
        Relation relation = new Relation();
        relation.setQuestionId(query.getQuestionId());
        relation.setKnowledgeId(query.getKnowledgeId());
        save(relation);
        // 发送ES同步更新消息
        questionService.sendUpdateMessage(query.getQuestionId());
    }

    @Override
    public void deleteRelation(RelationQuery query) {
        Relation relation = new Relation();
        relation.setQuestionId(query.getQuestionId());
        relation.setKnowledgeId(query.getKnowledgeId());
        // 检验是否解除关联成功
        boolean removed = remove(new LambdaQueryWrapper<Relation>()
                .eq(Relation::getQuestionId, query.getQuestionId())
                .eq(Relation::getKnowledgeId, query.getKnowledgeId()));
        // 如果解除关联成功，发送ES同步更新消息
        if (removed) {
            questionService.sendUpdateMessage(query.getQuestionId());
        }
    }
}
