package com.wx2.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.wx2.api.client.CollegeFeignClient;
import com.wx2.common.error.CollectionError;
import com.wx2.common.error.CollegeError;
import com.wx2.common.exception.BizException;
import com.wx2.common.utils.UserContext;
import com.wx2.user.mapper.UserCollectionCollegeMapper;
import com.wx2.user.model.entity.UserCollectionCollege;
import com.wx2.user.service.UserCollectionService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.wx2.common.constant.MqConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCollectionServiceImpl implements UserCollectionService {

    private final UserCollectionCollegeMapper userCollectionCollegeMapper;
    private final CollegeFeignClient collegeFeignClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @GlobalTransactional
    public void addCollectionCollege(Long collegeId) {
        Long userId = UserContext.getUserId();
        // 调用院校服务查询院校是否存在
        boolean collegeExists = collegeFeignClient.checkCollegeExists(collegeId);
        if (!collegeExists) {
            throw new BizException(CollegeError.NOT_EXIST);
        }
        // 检验用户是否已经收藏过此院校
        if (ObjectUtil.isNotNull(userCollectionCollegeMapper.selectUserIdAndCollegeId(userId, collegeId))) {
            throw new BizException(CollectionError.ALREADY_EXIST);
        }
        // 新增用户院校收藏
        UserCollectionCollege collection = new UserCollectionCollege();
        collection.setUserId(userId);
        collection.setCollegeId(collegeId);
        userCollectionCollegeMapper.insert(collection);
        // 使用消息队列通知院校增加收藏数
        try {
            rabbitTemplate.convertAndSend(COLLECTION_EXCHANGE, COLLECTION_COLLEGE_ADD_KEY, collegeId);
        } catch (AmqpException e) {
            log.error("收藏成功，通知院校收藏数增加服务失败", e);
        }

    }

    @Override
    @GlobalTransactional
    public void removeCollectionCollege(Long collegeId) {
        Long userId = UserContext.getUserId();
        // 检验用户是否未收藏此院校
        if (ObjectUtil.isNull(userCollectionCollegeMapper.selectUserIdAndCollegeId(userId, collegeId))) {
            throw new BizException(CollectionError.NOT_EXIST);
        }
        // 取消用户院校收藏
        userCollectionCollegeMapper.deleteByUserIdAndCollegeId(userId, collegeId);
        // 使用消息队列通知院校减少收藏数
        try {
            rabbitTemplate.convertAndSend(COLLECTION_EXCHANGE, COLLECTION_COLLEGE_REMOVE_KEY, collegeId);
        } catch (AmqpException e) {
            log.error("取消收藏成功，通知院校收藏数减少服务失败", e);
        }
    }
}
