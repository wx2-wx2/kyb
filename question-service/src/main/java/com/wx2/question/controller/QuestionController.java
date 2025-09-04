package com.wx2.question.controller;

import com.wx2.common.model.PageData;
import com.wx2.common.model.query.IdQuery;
import com.wx2.question.model.query.QuestionHotRankQuery;
import com.wx2.question.model.query.QuestionPageQuery;
import com.wx2.question.model.query.QuestionQuery;
import com.wx2.question.model.vo.QuestionHotRankVO;
import com.wx2.question.model.vo.QuestionVO;
import com.wx2.question.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 根据题目id查询题目
     */
    @GetMapping("/{questionId}")
    public QuestionVO getQuestionById(@PathVariable Long questionId,
                                      HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getRemoteAddr();
        return questionService.getQuestionById(questionId, ip);
    }

    /**
     * 分页查询题目
     */
    @PostMapping("/page")
    public PageData<QuestionVO> getQuestionByPage(@Valid @RequestBody QuestionPageQuery query) {
        return questionService.getQuestionByPage(query);
    }

    /**
     * 新增题目
     */
    @PostMapping("/add")
    public String addQuestion(@Valid @RequestBody QuestionQuery query) {
        questionService.addQuestion(query);
        return "新增题目成功";
    }

    /**
     * 更新题目
     */
    @PutMapping("/update")
    public String updateQuestion(@Valid @RequestBody QuestionQuery query) {
        questionService.updateQuestion(query);
        return "更新题目成功";
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/delete")
    public String deleteQuestion(@Valid @RequestBody IdQuery query) {
        questionService.deleteQuestion(query.getId());
        return "删除题目成功";
    }

    /**
     * 获取实时热度排行榜
     */
    @PostMapping("/rank")
    public List<QuestionHotRankVO> getQuestionHotRank(@Valid @RequestBody QuestionHotRankQuery query) {
        return questionService.getQuestionHotRank(query);
    }
}
