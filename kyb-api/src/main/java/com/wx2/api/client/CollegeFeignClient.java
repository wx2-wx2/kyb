package com.wx2.api.client;

import com.wx2.api.client.fallback.CollegeFeignClientFallbackFactory;
import com.wx2.api.config.FeignConfig;
import com.wx2.common.model.PageData;
import com.wx2.common.model.query.CollegePageQuery;
import com.wx2.common.model.vo.CollegeVO;
import com.wx2.common.model.vo.CollegeWithMajorVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "college-service",
        configuration = FeignConfig.class,
        fallbackFactory = CollegeFeignClientFallbackFactory.class
)
public interface CollegeFeignClient {
    @GetMapping("/api/college/check/{collegeId}")
    boolean checkCollegeExists(@PathVariable Long collegeId);

    @PostMapping("/api/college/batch-get")
    List<CollegeVO> getCollegeByIds(@RequestParam List<Long> collegeIds);

    @PostMapping("/api/college/page")
    PageData<CollegeVO> getCollegeByPage(@Valid @RequestBody CollegePageQuery query);

    @GetMapping("/api/college/{collegeId}")
    CollegeWithMajorVO getCollegeDetailInfoById(@PathVariable Long collegeId);
}
