package com.wx2.question.controller;

import com.wx2.question.model.query.RelationQuery;
import com.wx2.question.service.RelationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/relation")
public class RelationController {

    private final RelationService relationService;

    /**
     * 给指定题目关联知识点
     */
    @PostMapping("/add")
    public String addRelation(@Valid @RequestBody RelationQuery query) {
        relationService.addRelation(query);
        return "题目关联知识点成功";
    }

    /**
     * 解除关联
     */
    @DeleteMapping("/delete")
    public String deleteRelation(@Valid @RequestBody RelationQuery query) {
        relationService.deleteRelation(query);
        return "解除关联成功";
    }
}
