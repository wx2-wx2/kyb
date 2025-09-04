package com.wx2.user.service;

import com.wx2.common.model.query.UserBalanceQuery;
import com.wx2.user.model.query.SignQuery;
import com.wx2.user.model.query.UserLoginQuery;
import com.wx2.user.model.query.UserProfileQuery;
import com.wx2.user.model.query.UserRegisterQuery;
import com.wx2.user.model.vo.UserDetailVO;
import com.wx2.user.model.vo.UserLoginVO;

import java.util.List;

public interface UserService {
    /**
     * 登录
     */
    UserLoginVO login(UserLoginQuery query);

    /**
     * 注册
     */
    void register(UserRegisterQuery query);

    /**
     * 查询用户信息
     */
    UserDetailVO getUserInfo();

    /**
     * 更新用户信息
     */
    void updateUserProfile(UserProfileQuery query);

    /**
     * 增加用户余额
     */
    String increaseBalance(UserBalanceQuery query);

    /**
     * 扣减用户余额
     */
    String deductBalance(UserBalanceQuery query);

    /**
     * 用户签到
     */
    void sign();

    /**
     * 查询用户签到情况
     */
    List<Integer> getSign(SignQuery query);
}
