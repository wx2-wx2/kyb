package com.wx2.college.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx2.college.model.entity.College;
import com.wx2.common.model.query.CollegePageQuery;
import com.wx2.common.model.vo.CollegeVO;
import com.wx2.common.model.vo.CollegeWithMajorVO;
import com.wx2.college.model.vo.MajorDetailVO;
import com.wx2.common.model.PageData;

import java.util.List;

public interface CollegeService extends IService<College> {
    /**
     * 根据院校id查询院校（含专业列表）
     */
    CollegeWithMajorVO getCollegeDetailInfoById(Long collegeId, String ip);

    /**
     * 根据id批量查询院校
     */
    List<CollegeVO> getCollegeByIds(List<Long> collegeIds);

    /**
     * 分页查询院校
     */
    PageData<CollegeVO> getCollegeByPage(CollegePageQuery query);

    /**
     * 根据专业id查询历年数据
     */
    List<MajorDetailVO> getMajorDetailInfoById(Long majorId);

    /**
     * 查询院校是否存在
     */
    boolean checkCollegeExists(Long collegeId);

    /**
     * 更新院校收藏数
     */
    void updateCollegeCollection(Long collegeId, Integer change);
}
