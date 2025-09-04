package com.wx2.college.listener;

import com.wx2.college.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.wx2.common.constant.MqConstant.*;

@Component
@RequiredArgsConstructor
public class CollectionListener {

    private final CollegeService collegeService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = COLLECTION_COLLEGE_QUEUE, durable = "true"),
            exchange = @Exchange(name = COLLECTION_EXCHANGE, type = "topic"),
            key = COLLECTION_COLLEGE_ADD_KEY
    ))
    public void listenCollectionAdd(Long collegeId) {
        collegeService.updateCollegeCollection(collegeId, 1);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = COLLECTION_COLLEGE_QUEUE, durable = "true"),
            exchange = @Exchange(name = COLLECTION_EXCHANGE, type = "topic"),
            key = COLLECTION_COLLEGE_REMOVE_KEY
    ))
    public void listenCollectionRemove(Long collegeId) {
        collegeService.updateCollegeCollection(collegeId, -1);
    }
}
