package com.wx2.api.client.fallback;

import com.wx2.api.client.CollegeFeignClient;
import com.wx2.common.model.PageData;
import com.wx2.common.model.query.CollegePageQuery;
import com.wx2.common.model.vo.CollegeVO;
import com.wx2.common.model.vo.CollegeWithMajorVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CollegeFeignClientFallbackFactory implements FallbackFactory<CollegeFeignClient> {
    @Override
    public CollegeFeignClient create(Throwable cause) {
        return new CollegeFeignClient() {

            @Override
            public boolean checkCollegeExists(Long collegeId) {
                log.error("检查院校失败", cause);
                return false;
            }

            @Override
            public List<CollegeVO> getCollegeByIds(List<Long> collegeIds) {
                log.error("获取院校失败", cause);
                return List.of();
            }

            @Override
            public PageData<CollegeVO> getCollegeByPage(CollegePageQuery query) {
                log.error("获取院校失败", cause);
                return new PageData<>(List.of(), 0, query);
            }

            @Override
            public CollegeWithMajorVO getCollegeDetailInfoById(Long collegeId) {
                log.error("获取院校失败", cause);
                return new CollegeWithMajorVO();
            }
        };
    }
}
