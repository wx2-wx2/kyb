package com.wx2.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wx2.common.error.UserError;
import com.wx2.common.exception.BizException;
import com.wx2.common.utils.UserContext;
import com.wx2.user.config.JwtProperties;
import com.wx2.user.enums.UserStatus;
import com.wx2.user.mapper.UserMapper;
import com.wx2.user.mapper.UserProfileMapper;
import com.wx2.user.model.entity.User;
import com.wx2.user.model.entity.UserProfile;
import com.wx2.common.model.query.UserBalanceQuery;
import com.wx2.user.model.query.SignQuery;
import com.wx2.user.model.query.UserLoginQuery;
import com.wx2.user.model.query.UserProfileQuery;
import com.wx2.user.model.query.UserRegisterQuery;
import com.wx2.user.model.vo.UserDetailVO;
import com.wx2.user.model.vo.UserLoginVO;
import com.wx2.user.service.UserService;
import com.wx2.user.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wx2.common.constant.RedisConstant.USER_SIGN_PREFIX;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTool jwtTool;
    private final JwtProperties jwtProperties;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public UserLoginVO login(UserLoginQuery query) {
        String account = query.getAccount();
        String password = query.getPassword();
        // 检验用户是否存在
        User user = userMapper.selectByAccount(account);
        if (ObjectUtil.isNull(user)) {
            throw new BizException(UserError.NOT_EXIST);
        }
        // 检验用户状态是否正常
        if (!Objects.equals(user.getStatus(), UserStatus.NORMAL.getCode())) {
            throw new BizException(UserError.STATUS_ERROR);
        }
        // 检验用户密码是否正确
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException(UserError.PASSWORD_ERROR);
        }
        // 生成token
        String token = jwtTool.createToken(user.getId(), jwtProperties.getTokenTTL());
        // 更新用户最近登录时间
        userMapper.updateLastLoginTime(user.getId());
        // 返回用户登录信息
        UserLoginVO vo = new UserLoginVO();
        vo.setUserId(user.getId());
        vo.setToken(token);

        return vo;
    }

    @Override
    @Transactional
    public void register(UserRegisterQuery query) {
        String username = query.getUsername();
        String password = query.getPassword();
        // 检验用户是否已存在
        User user = userMapper.selectByAccount(username);
        if (ObjectUtil.isNotNull(user)) {
            throw new BizException(UserError.USERNAME_DUPLICATE);
        }
        // 密码加密
        String encryptPassword = passwordEncoder.encode(password);
        // 新增用户
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptPassword);
        userMapper.insert(newUser);
    }

    @Override
    public UserDetailVO getUserInfo() {
        Long userId = UserContext.getUserId();
        // 检验用户是否存在
        User user = userMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new BizException(UserError.NOT_EXIST);
        }
        // 检验用户状态是否正常
        if (user.getStatus() != 1) {
            throw new BizException(UserError.STATUS_ERROR);
        }
        // 查询用户详细信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);

        UserDetailVO vo = new UserDetailVO();
        // 拷贝用户核心信息
        vo.setUserId(userId);
        BeanUtil.copyProperties(user, vo);
        // 拷贝用户详细信息
        if (ObjectUtil.isNotNull(userProfile)) {
            BeanUtil.copyProperties(userProfile, vo);
        }

        return vo;
    }

    @Override
    @Transactional
    public void updateUserProfile(UserProfileQuery query) {
        Long userId = UserContext.getUserId();
        // 检验用户是否存在
        User user = userMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new BizException(UserError.NOT_EXIST);
        }
        // 查询用户详细信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);
        userProfile.setUserId(userId);
        // 如果用户详细信息不存在则新增，否则更新
        if (ObjectUtil.isNull(userProfile)) {
            userProfile = new UserProfile();
            BeanUtil.copyProperties(query, userProfile);
            userProfileMapper.insertByUserId(userProfile);
        }
        else {
            BeanUtil.copyProperties(query, userProfile);
            userProfileMapper.updateByUserId(userProfile);
        }
    }

    @Override
    @Transactional
    public String increaseBalance(UserBalanceQuery query) {
        Long userId = query.getUserId();
        BigDecimal amount = query.getAmount();
        // 查询并更新用户余额
        User user = userMapper.selectById(userId);
        userMapper.updateBalance(userId, amount);
        // 返回用户当前余额
        return user.getBalance().add(amount).toString();
    }

    @Override
    @Transactional
    public String deductBalance(UserBalanceQuery query) {
        Long userId = query.getUserId();
        String password = query.getPassword();
        BigDecimal amount = query.getAmount();
        User user = userMapper.selectById(userId);
        // 检验密码是否正确
        if (ObjectUtil.isNull(user) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException(UserError.PASSWORD_ERROR);
        }
        // 检验用户余额被扣减后是否小于0
        if (user.getBalance().compareTo(amount) < 0) {
            throw new BizException(UserError.BALANCE_INSUFFICIENT);
        }
        // 更新用户余额
        userMapper.updateBalance(userId, amount);
        // 返回用户当前余额
        return user.getBalance().add(amount).toString();
    }

    @Override
    public void sign() {
        Long userId = UserContext.getUserId();
        LocalDateTime now = LocalDateTime.now();
        // 格式化当前年月作为redis键的后缀
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_PREFIX + userId + keySuffix;
        int dayOfMonth = now.getDayOfMonth();
        // 使用redis的BitMap结构记录签到状态,将对应天数的位设置为1
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
    }

    @Override
    public List<Integer> getSign(SignQuery query) {
        Integer year = query.getYear();
        Integer month = query.getMonth();
        Long userId = UserContext.getUserId();
        List<Integer> signDays = new ArrayList<>();
        // 构建目标月份的redis查询键
        LocalDate date = LocalDate.of(year, month, 1);
        String keySuffix = date.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = USER_SIGN_PREFIX + userId + keySuffix;
        // 获取当月总天数
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        // 遍历查询每天的签到状态
        for (int i = 0; i < daysInMonth; i++) {
            Boolean isSigned = stringRedisTemplate.opsForValue().getBit(key, i);
            if (Boolean.TRUE.equals(isSigned)) {
                signDays.add(i + 1);
            }
        }

        return signDays;
    }
}
