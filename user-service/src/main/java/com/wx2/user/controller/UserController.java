package com.wx2.user.controller;

import com.wx2.common.model.query.UserBalanceQuery;
import com.wx2.user.model.query.SignQuery;
import com.wx2.user.model.query.UserLoginQuery;
import com.wx2.user.model.query.UserProfileQuery;
import com.wx2.user.model.query.UserRegisterQuery;
import com.wx2.user.model.vo.UserDetailVO;
import com.wx2.user.model.vo.UserLoginVO;
import com.wx2.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public UserLoginVO login(@Valid @RequestBody UserLoginQuery query) {
        return userService.login(query);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRegisterQuery query) {
        userService.register(query);
        return "注册成功";
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/get")
    public UserDetailVO getUserInfo() {
        return userService.getUserInfo();
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public String updateUserProfile(@Valid @RequestBody UserProfileQuery query) {
        userService.updateUserProfile(query);
        return "修改成功";
    }

    /**
     * 增加用户余额
     */
    @PutMapping("/balance/increase")
    public String increaseBalance(@Valid @RequestBody UserBalanceQuery query) {
        return userService.increaseBalance(query);
    }

    /**
     * 扣减用户余额
     */
    @PutMapping("/balance/deduct")
    public String deductBalance(@Valid @RequestBody UserBalanceQuery query) {
        return userService.deductBalance(query);
    }

    /**
     * 用户签到
     */
    @PostMapping("/sign")
    public String sign() {
        userService.sign();
        return "签到成功";
    }

    /**
     * 查询用户签到情况
     */
    @GetMapping("/sign/get")
    public List<Integer> getSign(@Valid @RequestBody SignQuery query) {
        return userService.getSign(query);
    }
}
