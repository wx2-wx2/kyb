package com.wx2.college.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx2.college.enums.RankType;
import com.wx2.college.mapper.CollegeHotRankMapper;
import com.wx2.college.mapper.CollegeMapper;
import com.wx2.college.mapper.CollegeSearchLogMapper;
import com.wx2.college.model.dto.ValidSearchDTO;
import com.wx2.college.model.entity.College;
import com.wx2.college.model.entity.CollegeHotRank;
import com.wx2.college.model.query.CollegeHotRankQuery;
import com.wx2.college.model.vo.CollegeHotRankVO;
import com.wx2.college.service.CollegeRankService;
import com.wx2.common.error.RankError;
import com.wx2.common.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollegeRankServiceImpl extends ServiceImpl<CollegeHotRankMapper, CollegeHotRank> implements CollegeRankService {

    private final CollegeMapper collegeMapper;
    private final CollegeSearchLogMapper collegeSearchLogMapper;
    private final CollegeHotRankMapper collegeHotRankMapper;

    @Override
    @Transactional
    public void updateDailyHotScore() {
        // 计算时间范围
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startTime = LocalDateTime.of(yesterday, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(yesterday, LocalTime.MAX);

        try {
            // 查询有效搜索记录
            List<ValidSearchDTO> validSearchList = collegeSearchLogMapper.selectValidSearchData(startTime, endTime);
            // 根据搜索记录计算热度值
            Map<Long, Integer> collegeScoreMap = calculateHotScores(validSearchList);
            // 批量更新热度值
            collegeMapper.batchUpdateHotScore(collegeScoreMap);

            log.info("{} 院校每日热度值更新完成，热度值大于0的院校共{}所", yesterday, collegeScoreMap.size());
        } catch (Exception e) {
            log.info("{} 院校每日热度值更新失败：{}", yesterday, e.getMessage());
        }

    }

    @Override
    @Transactional
    public void generateDailyHotRank() {
        // 计算榜单类型和时间范围
        Integer rankType = RankType.getByDesc("日榜").getCode();
        LocalDate rankDate = LocalDate.now().minusDays(1);
        LocalDateTime startTime = rankDate.atStartOfDay();
        LocalDateTime endTime = rankDate.atTime(23, 59, 59);

        try {
            // 生成榜单
            generateRank(rankType, rankDate, startTime, endTime);

            log.info("{} 热度日榜榜单生成完成", rankDate);
        } catch (Exception e) {
            log.info("{} 热度日榜榜单生成失败", rankDate);
        }
    }

    @Override
    @Transactional
    public void generateWeeklyHotRank() {
        // 计算榜单类型和时间范围
        LocalDate today = LocalDate.now();
        LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate lastSunday = thisMonday.minusDays(1);
        LocalDate lastMonday = lastSunday.minusDays(6);
        LocalDateTime startTime = lastMonday.atStartOfDay();
        LocalDateTime endTime = lastSunday.atTime(23, 59, 59);

        Integer rankType = RankType.getByDesc("周榜").getCode();
        LocalDate rankDate = endTime.toLocalDate();

        try {
            // 生成榜单
            generateRank(rankType, rankDate, startTime, endTime);

            log.info("{} 热度周榜榜单生成完成", rankDate);
        } catch (Exception e) {
            log.info("{} 热度周榜榜单生成失败", rankDate);
        }
    }

    @Override
    @Transactional
    public void generateMonthlyHotRank() {
        // 计算榜单类型和时间范围
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = firstDayOfCurrentMonth.minusDays(1);
        LocalDate firstDayOfLastMonth = lastDayOfLastMonth.withDayOfMonth(1);
        LocalDateTime startTime = firstDayOfLastMonth.atStartOfDay();
        LocalDateTime endTime = lastDayOfLastMonth.atTime(23, 59, 59);

        Integer rankType = RankType.getByDesc("月榜").getCode();
        LocalDate rankDate = endTime.toLocalDate();

        try {
            // 生成榜单
            generateRank(rankType, rankDate, startTime, endTime);

            log.info("{} 热度月榜榜单生成完成", rankDate);
        } catch (Exception e) {
            log.info("{} 热度月榜榜单生成失败", rankDate);
        }
    }

    @Override
    public List<CollegeHotRankVO> getCollegeHotRank(CollegeHotRankQuery query) {
        Integer rankType = query.getRankType();
        LocalDate rankDate = query.getRankDate();
        List<CollegeHotRank> rankList = collegeHotRankMapper.selectByTypeAndDate(rankType, rankDate);

        if (rankList.isEmpty()) {
            throw new BizException(RankError.NOT_EXIST);
        }

        List<Long> collegeIds = rankList.stream()
                .map(CollegeHotRank::getCollegeId)
                .distinct()
                .collect(Collectors.toList());

        List<College> colleges = collegeMapper.selectBatchIds(collegeIds);
        Map<Long, String> collegeNameMap = colleges.stream()
                .collect(Collectors.toMap(College::getId, College::getCollegeName));

        return rankList.stream()
                .map(rank -> {
                    CollegeHotRankVO vo = new CollegeHotRankVO();
                    BeanUtil.copyProperties(rank, vo);
                    vo.setCollegeName(collegeNameMap.getOrDefault(rank.getCollegeId(), "未知院校"));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    private void generateRank(Integer rankType, LocalDate rankDate, LocalDateTime startTime, LocalDateTime endTime) {
        // 查询有效搜索记录
        List<ValidSearchDTO> validSearchList = collegeSearchLogMapper.selectValidSearchData(startTime, endTime);
        // 根据搜索记录计算热度值
        Map<Long, Integer> collegeScoreMap = calculateHotScores(validSearchList);
        // 对院校排序（先按热度值降序，再按院校id升序）
        List<Map.Entry<Long, Integer>> sortedEntries = collegeScoreMap.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int scoreCompare = entry2.getValue().compareTo(entry1.getValue());
                    if (scoreCompare != 0) {
                        return scoreCompare;
                    }
                    return entry1.getKey().compareTo(entry2.getKey());
                })
                .toList();
        // 封装榜单数据
        List<CollegeHotRank> rankList = new ArrayList<>();
        for (int i = 0; i < sortedEntries.size() && i < 100; i++) {
            Map.Entry<Long, Integer> entry = sortedEntries.get(i);
            CollegeHotRank rank = new CollegeHotRank();
            rank.setRankType(rankType);
            rank.setCollegeId(entry.getKey());
            rank.setRank(i + 1);
            rank.setHotScore(entry.getValue());
            rank.setRankDate(rankDate);
            rankList.add(rank);
        }
        // 去重
        collegeHotRankMapper.deleteByRankTypeAndDate(rankType, rankDate);
        // 新增榜单数据
        if (CollectionUtil.isNotEmpty(rankList)) {
            collegeHotRankMapper.batchInsert(rankList);
        }
    }

    private Map<Long, Integer> calculateHotScores(List<ValidSearchDTO> validSearchList) {
        Map<Long, Integer> scoreMap = new HashMap<>();
        // 计算院校热度值（基础1分，点击额外加2分）
        for (ValidSearchDTO dto : validSearchList) {
            Long collegeId = dto.getCollegeId();
            Integer isClick = dto.getIsClick();
            scoreMap.put(collegeId, scoreMap.getOrDefault(collegeId, 0) + 1 + (isClick == 1 ? 2 : 0));
        }

        return scoreMap;
    }
}
