package com.wx2.college.mapper;

import com.wx2.college.model.entity.CollegeMajor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollegeMajorMapper {
    /**
     * 根据院校id查询专业信息
     */
    List<CollegeMajor> selectByCollegeId(@Param("collegeId") Long collegeId);
}
