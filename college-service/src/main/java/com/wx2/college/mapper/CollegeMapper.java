package com.wx2.college.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx2.college.model.entity.College;
import com.wx2.common.model.query.CollegePageQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
    /**
     * 根据id查询院校基础信息
     */
    College selectById(@Param("id") Long id);

    /**
     * 根据查询条件统计总记录数
     */
    long countByQuery(CollegePageQuery query);

    /**
     * 根据查询条件分页查询院校
     */
    List<College> selectByQuery(CollegePageQuery query);

    /**
     * 查询所有院校id
     */
    List<Long> selectAllCollegeIds();

    /**
     * 批量更新院校热度值
     */
    void batchUpdateHotScore(@Param("scoreMap") Map<Long, Integer> scoreMap);

    /**
     * 查询所有院校，按hotScore降序
     */
    List<College> selectAllOrderByHotScore();

    /**
     * 根据id批量查询数据
     */
    List<College> selectBatchIds(@Param("ids") List<Long> ids);

    /**
     * 更新院校收藏数
     */
    void updateCollectCountById(@Param("id") Long id, @Param("change") Integer change);
}
