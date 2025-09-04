package com.wx2.college.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx2.college.model.entity.CollegeHotRank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CollegeHotRankMapper extends BaseMapper<CollegeHotRank> {

    /**
     * 按榜单类型和日期删除旧数据
     */
    void deleteByRankTypeAndDate(@Param("rankType") Integer rankType, @Param("rankDate") LocalDate rankDate);

    /**
     * 批量插入榜单数据
     */
    void batchInsert(@Param("rankList") List<CollegeHotRank> rankList);

    /**
     * 根据类型和日期查询榜单
     */
    List<CollegeHotRank> selectByTypeAndDate(@Param("rankType") Integer rankType, @Param("rankDate") LocalDate rankDate);
}
