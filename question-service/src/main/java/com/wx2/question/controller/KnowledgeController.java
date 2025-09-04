package com.wx2.question.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wx2.common.model.query.IdQuery;
import com.wx2.question.model.query.KnowledgeQuery;
import com.wx2.question.model.vo.KnowledgeVO;
import com.wx2.question.service.KnowledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    /**
     * 根据知识点id查询知识点
     */
    @GetMapping("/{knowledgeId}")
    public KnowledgeVO getKnowledgeById(@PathVariable Long knowledgeId) {
        return BeanUtil.copyProperties(knowledgeService.getById(knowledgeId), KnowledgeVO.class);
    }

    /**
     * 根据学科查询知识点
     */
    @GetMapping("/by-subject/{subjectId}")
    public List<KnowledgeVO> getKnowledgeBySubjectId(@PathVariable Long subjectId) {
        return knowledgeService.getKnowledgeBySubject(subjectId);
    }

    /**
     * 新增知识点
     */
    @PostMapping("/add")
    public String addKnowledge(@Valid @RequestBody KnowledgeQuery query) {
        knowledgeService.addKnowledge(query);
        return "新增知识点成功";
    }

    /**
     * 更新知识点
     */
    @PutMapping("/update")
    public String updateKnowledge(@Valid @RequestBody KnowledgeQuery query) {
        knowledgeService.updateKnowledge(query);
        return "更新知识点成功";
    }

    /**
     * 删除知识点
     */
    @DeleteMapping("/delete")
    public String deleteKnowledge(@Valid @RequestBody IdQuery query) {
        knowledgeService.deleteKnowledge(query.getId());
        return "删除知识点成功";
    }
}
