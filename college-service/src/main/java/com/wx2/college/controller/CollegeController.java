package com.wx2.college.controller;

import com.wx2.college.model.query.CollegeHotRankQuery;
import com.wx2.common.model.query.CollegePageQuery;
import com.wx2.college.model.vo.CollegeHotRankVO;
import com.wx2.common.model.vo.CollegeVO;
import com.wx2.common.model.vo.CollegeWithMajorVO;
import com.wx2.college.model.vo.MajorDetailVO;
import com.wx2.college.service.CollegeRankService;
import com.wx2.college.service.CollegeService;
import com.wx2.common.model.PageData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/college")
public class CollegeController {

    private final CollegeService collegeService;
    private final CollegeRankService collegeRankService;

    /**
     * 根据院校id查询院校（含专业列表）
     */
    @GetMapping("/{collegeId}")
    public CollegeWithMajorVO getCollegeDetailInfoById(@PathVariable Long collegeId,
                                                      HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getRemoteAddr();
        return collegeService.getCollegeDetailInfoById(collegeId, ip);
    }

    /**
     * 根据院校id批量查询院校
     */
    @PostMapping("/batch-get")
    public List<CollegeVO> getCollegeByIds(@RequestParam List<Long> collegeIds) {
        return collegeService.getCollegeByIds(collegeIds);
    }

    /**
     * 分页查询院校
     */
    @PostMapping("/page")
    public PageData<CollegeVO> getCollegeByPage(@Valid @RequestBody CollegePageQuery query) {
        return collegeService.getCollegeByPage(query);
    }

    /**
     * 根据专业id查询历年数据
     */
    @GetMapping("/major/{majorId}")
    public List<MajorDetailVO> getMajorDetailInfoById(@PathVariable Long majorId) {
        return collegeService.getMajorDetailInfoById(majorId);
    }

    /**
     * 查询院校是否存在
     */
    @GetMapping("/check/{collegeId}")
    public boolean checkCollegeExists(@PathVariable Long collegeId) {
        return collegeService.checkCollegeExists(collegeId);
    }

    /**
     * 获取院校榜单
     */
    @PostMapping("/rank")
    public List<CollegeHotRankVO> getCollegeHotRank(@Validated @RequestBody CollegeHotRankQuery query) {
        return collegeRankService.getCollegeHotRank(query);
    }
}
