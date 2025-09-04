package com.wx2.college.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.college.enums.*;
import com.wx2.college.mapper.CollegeMajorMapper;
import com.wx2.college.mapper.CollegeMajorYearDataMapper;
import com.wx2.college.mapper.CollegeMapper;
import com.wx2.college.mapper.CollegeSearchLogMapper;
import com.wx2.college.model.entity.College;
import com.wx2.college.model.entity.CollegeMajor;
import com.wx2.college.model.entity.CollegeMajorYearData;
import com.wx2.common.model.query.CollegePageQuery;
import com.wx2.common.model.vo.CollegeVO;
import com.wx2.common.model.vo.CollegeWithMajorVO;
import com.wx2.college.model.vo.MajorDetailVO;
import com.wx2.common.model.vo.MajorVO;
import com.wx2.college.service.CollegeService;
import com.wx2.common.error.CollegeError;
import com.wx2.common.exception.BizException;
import com.wx2.common.model.PageData;
import com.wx2.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wx2.common.constant.RedisConstant.*;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {

    private final CollegeMapper collegeMapper;
    private final CollegeMajorMapper collegeMajorMapper;
    private final CollegeMajorYearDataMapper collegeMajorYearDataMapper;
    private final CollegeSearchLogMapper collegeSearchLogMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public CollegeWithMajorVO getCollegeDetailInfoById(Long collegeId, String ip) {
        // 查询并检验院校是否为空
        CollegeWithMajorVO collegeWithMajorVO = getWithMutex(collegeId);
        if (ObjectUtil.isNull(collegeWithMajorVO)) {
            throw new BizException(CollegeError.NOT_EXIST);
        }
        // 新增院校搜索日志
        Long userId = UserContext.getUserId();
        collegeSearchLogMapper.insertByCollegeIdAndIp(userId, collegeId, ip);

        return collegeWithMajorVO;
    }

    @Override
    public List<CollegeVO> getCollegeByIds(List<Long> collegeIds) {
        // 查询院校基本信息并设置院校层次、类型、性质等描述字段
        List<College> collegeList = collegeMapper.selectBatchIds(collegeIds);

        return collegeList.stream()
                .map(college -> {
                    CollegeVO vo = new CollegeVO();
                    BeanUtil.copyProperties(college, vo);
                    vo.setLevelDesc(
                            CollegeLevel.getByCode(college.getLevel()) != null ?
                                    Objects.requireNonNull(CollegeLevel.getByCode(college.getLevel())).getDesc() : "未知"
                    );
                    vo.setTypeDesc(
                            CollegeType.getByCode(college.getType()) != null ?
                                    Objects.requireNonNull(CollegeType.getByCode(college.getType())).getDesc() : "未知"
                    );
                    vo.setNatureDesc(
                            CollegeNature.getByCode(college.getNature()) != null ?
                                    Objects.requireNonNull(CollegeNature.getByCode(college.getNature())).getDesc() : "未知"
                    );
                    vo.setHasGraduateSchoolDesc(
                            CollegeHasGraduateSchool.getByCode(college.getHasGraduateSchool()) != null ?
                                    Objects.requireNonNull(CollegeHasGraduateSchool.getByCode(college.getHasGraduateSchool())).getDesc() : "未知"
                    );
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageData<CollegeVO> getCollegeByPage(CollegePageQuery query) {
        // 计算总记录
        long total = collegeMapper.countByQuery(query);
        // 分页查询院校
        List<College> collegeList = collegeMapper.selectByQuery(query);
        // 设置院校层次、类型、性质等描述字段
        List<CollegeVO> voList = collegeList.stream()
                .map(college -> {
                    CollegeVO vo = new CollegeVO();
                    BeanUtil.copyProperties(college, vo);
                    vo.setLevelDesc(
                            CollegeLevel.getByCode(college.getLevel()) != null ?
                                    Objects.requireNonNull(CollegeLevel.getByCode(college.getLevel())).getDesc() : "未知"
                    );
                    vo.setTypeDesc(
                            CollegeType.getByCode(college.getType()) != null ?
                                    Objects.requireNonNull(CollegeType.getByCode(college.getType())).getDesc() : "未知"
                    );
                    vo.setNatureDesc(
                            CollegeNature.getByCode(college.getNature()) != null ?
                                    Objects.requireNonNull(CollegeNature.getByCode(college.getNature())).getDesc() : "未知"
                    );
                    vo.setHasGraduateSchoolDesc(
                            CollegeHasGraduateSchool.getByCode(college.getHasGraduateSchool()) != null ?
                                    Objects.requireNonNull(CollegeHasGraduateSchool.getByCode(college.getHasGraduateSchool())).getDesc() : "未知"
                    );
                    return vo;
                })
                .collect(Collectors.toList());
        // 封装并返回分页数据对象
        return new PageData<>(voList, total, query);
    }

    @Override
    public List<MajorDetailVO> getMajorDetailInfoById(Long majorId) {
        // 查询专业历年数据
        List<CollegeMajorYearData> collegeMajorYearDataList = collegeMajorYearDataMapper.selectByMajorId(majorId);

        return collegeMajorYearDataList.stream()
                .map(data -> {
                    MajorDetailVO majorDetailVO = new MajorDetailVO();
                    BeanUtil.copyProperties(data, majorDetailVO);
                    return majorDetailVO;
                })
                .sorted((d1, d2) -> d2.getYear() - d1.getYear())
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkCollegeExists(Long collegeId) {
        // 查询院校是否存在
        College college = collegeMapper.selectById(collegeId);
        return !ObjectUtil.isNull(college);
    }

    @Override
    @Transactional
    public void updateCollegeCollection(Long collegeId, Integer change) {
        // 更新院校收藏数
        collegeMapper.updateCollectCountById(collegeId, change);
    }

    private CollegeWithMajorVO getWithMutex(Long collegeId) {
        String cacheKey = CACHE_COLLEGE_PREFIX + collegeId;
        // 从redis查询题目
        String collegeJson = stringRedisTemplate.opsForValue().get(cacheKey);
        // 缓存命中直接返回
        if (StrUtil.isNotBlank(collegeJson)) {
            return JSONUtil.toBean(collegeJson, CollegeWithMajorVO.class);
        }
        if (collegeId != null) {
            return null;
        }
        // 缓存重建
        String lockKey = COLLEGE_LOCK_PREFIX + collegeId;
        CollegeWithMajorVO collegeWithMajorVO = new CollegeWithMajorVO();

        try {
            // 尝试获取互斥锁
            boolean isLock = tryLock(lockKey);
            // 未获取到锁，50m后重试
            if (!isLock) {
                Thread.sleep(50);
                return getWithMutex(collegeId);
            }
            // 从数据库查询院校
            College college = collegeMapper.selectById(collegeId);
            // 题目不存在，缓存空值
            if (ObjectUtil.isNull(college)) {
                stringRedisTemplate.opsForValue().set(cacheKey, "", NULL_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            }
            // 查询院校对应专业信息
            List<CollegeMajor> majorList = collegeMajorMapper.selectByCollegeId(collegeId);

            BeanUtil.copyProperties(college, collegeWithMajorVO);
            // 设置院校层次、类型、性质等描述字段
            collegeWithMajorVO.setLevelDesc(
                    CollegeLevel.getByCode(college.getLevel()) != null ?
                            Objects.requireNonNull(CollegeLevel.getByCode(college.getLevel())).getDesc() : "未知"
            );
            collegeWithMajorVO.setTypeDesc(
                    CollegeType.getByCode(college.getType()) != null ?
                            Objects.requireNonNull(CollegeType.getByCode(college.getType())).getDesc() : "未知"
            );
            collegeWithMajorVO.setNatureDesc(
                    CollegeNature.getByCode(college.getNature()) != null ?
                            Objects.requireNonNull(CollegeNature.getByCode(college.getNature())).getDesc() : "未知"
            );
            collegeWithMajorVO.setHasGraduateSchoolDesc(
                    CollegeHasGraduateSchool.getByCode(college.getHasGraduateSchool()) != null ?
                            Objects.requireNonNull(CollegeHasGraduateSchool.getByCode(college.getHasGraduateSchool())).getDesc() : "未知"
            );
            // 设置专业学位类型和学科描述字段
            List<MajorVO> majorVOList = majorList.stream()
                    .map(major -> {
                        MajorVO majorVO = new MajorVO();
                        BeanUtil.copyProperties(major, majorVO);
                        majorVO.setDegreeTypeDesc(
                                MajorDegreeType.getByCode(major.getDegreeType()) != null ?
                                        Objects.requireNonNull(MajorDegreeType.getByCode(major.getDegreeType())).getDesc() : "未知"
                        );
                        majorVO.setDisciplineDesc(
                                MajorDiscipline.getByCode(major.getDiscipline()) != null ?
                                        Objects.requireNonNull(MajorDiscipline.getByCode(major.getDiscipline())).getDesc() : "未知"
                        );
                        return majorVO;
                    })
                    .collect(Collectors.toList());

            collegeWithMajorVO.setMajor(majorVOList);
            // 存入redis
            stringRedisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(collegeWithMajorVO), EXPIRATION_MINUTES, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放互斥锁
            unlock(lockKey);
        }
        return collegeWithMajorVO;
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
