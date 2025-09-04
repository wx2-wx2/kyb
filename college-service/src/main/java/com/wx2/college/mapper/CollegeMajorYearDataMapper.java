package com.wx2.college.mapper;

import com.wx2.college.model.entity.CollegeMajorYearData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollegeMajorYearDataMapper {
    /**
     * 根据专业id查询历年数据
     */
    List<CollegeMajorYearData> selectByMajorId(@Param("majorId") Long majorId);
}
