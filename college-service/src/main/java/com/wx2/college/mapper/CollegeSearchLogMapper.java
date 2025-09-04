package com.wx2.college.mapper;

import com.wx2.college.model.dto.ValidSearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CollegeSearchLogMapper {
    /**
     * 新增院校查询日志
     */
    void insertByCollegeIdAndIp(@Param("userId") Long userId, @Param("collegeId") Long collegeId, @Param("ip") String ip);

    /**
     * 查询指定时间范围内的有效搜索记录
     */
    List<ValidSearchDTO> selectValidSearchData(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
