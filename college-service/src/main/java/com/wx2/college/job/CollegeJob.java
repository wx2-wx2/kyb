package com.wx2.college.job;

import com.wx2.college.service.CollegeRankService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollegeJob {

    private final CollegeRankService collegeRankService;

    // 院校每日热度值更新
    @XxlJob("collegeHotRankUpdate")
    public ReturnT<String> collegeHotRankUpdate() {
        collegeRankService.updateDailyHotScore();
        return ReturnT.SUCCESS;
    }

    // 院校日榜生成
    @XxlJob("collegeDailyHotRank")
    public ReturnT<String> collegeDailyHotRank() {
        collegeRankService.generateDailyHotRank();
        return ReturnT.SUCCESS;
    }

    // 院校周榜生成
    @XxlJob("collegeWeeklyHotRank")
    public ReturnT<String> collegeWeeklyHotRank() {
        collegeRankService.generateWeeklyHotRank();
        return ReturnT.SUCCESS;
    }

    // 院校月榜生成
    @XxlJob("collegeMonthlyHotRank")
    public ReturnT<String> collegeMonthlyHotRank() {
        collegeRankService.generateMonthlyHotRank();
        return ReturnT.SUCCESS;
    }
}
