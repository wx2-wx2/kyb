package com.wx2.user.controller;

import com.wx2.common.model.query.IdQuery;
import com.wx2.user.service.UserCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collection")
@RequiredArgsConstructor
public class UserCollectionController {

    private final UserCollectionService userCollectionService;

    /**
     * 收藏院校
     */
    @PostMapping("/college/add")
    public String addCollectionCollege(@Validated @RequestBody IdQuery query) {
        userCollectionService.addCollectionCollege(query.getId());
        return "收藏院校成功";
    }

    /**
     * 取消收藏院校
     */
    @PostMapping("/college/remove")
    public String removeCollectionCollege(@Validated @RequestBody IdQuery query) {
        userCollectionService.removeCollectionCollege(query.getId());
        return "取消收藏院校成功";
    }
}
