package com.wx2.college.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.college.model.entity.CollegeHotRank;
import com.wx2.college.model.query.CollegeHotRankQuery;
import com.wx2.college.model.vo.CollegeHotRankVO;

import java.util.List;

public interface CollegeRankService extends IService<CollegeHotRank> {
    /**
     * 更新每日热度值
     */
    void updateDailyHotScore();

    /**
     * 生成热度日榜
     */
    void generateDailyHotRank();

    /**
     * 生成热度周榜
     */
    void generateWeeklyHotRank();

    /**
     * 生成热度月榜
     */
    void generateMonthlyHotRank();

    /**
     * 获取榜单信息
     */
    List<CollegeHotRankVO> getCollegeHotRank(CollegeHotRankQuery query);
}
