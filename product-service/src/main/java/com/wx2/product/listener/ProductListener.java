package com.wx2.product.listener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.wx2.product.model.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

import static com.wx2.common.constant.MqConstant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductListener {

    private final ElasticsearchClient elasticsearchClient;

    @RabbitListener(
            containerFactory = "manualAckContainerFactory", // 绑定手动工厂
            bindings = @QueueBinding(
            value = @Queue(value = ES_PRODUCT_UPDATE_QUEUE, durable = "true"),
            exchange = @Exchange(name = ES_EXCHANGE, type = "topic"),
            key = ES_PRODUCT_UPDATE_KEY
    ))
    public void addToEs(Product product,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                         Channel channel) {
        boolean processedSuccessfully = false;

        try {
            IndexRequest<Product> request = IndexRequest.of(req -> req
                    .index("product")
                    .id(product.getId().toString())
                    .document(product)
            );

            elasticsearchClient.index(request);
            log.info("ES商品同步成功，商品id: {}", product.getId());

            processedSuccessfully = true;

        } catch (Exception e) {
            log.error("商品同步到ES失败", e);
        } finally {
            try {
                if (channel != null && channel.isOpen() && deliveryTag > 0) {
                    if (processedSuccessfully) {
                        channel.basicAck(deliveryTag, false);
                    } else {
                        channel.basicNack(deliveryTag, false, true);
                    }
                }
            } catch (IOException e) {
                log.error("更新消息确认失败", e);
            }
        }
    }

    @RabbitListener(
            containerFactory = "manualAckContainerFactory",
            bindings = @QueueBinding(
            value = @Queue(value = ES_PRODUCT_DELETE_QUEUE, durable = "true"),
            exchange = @Exchange(name = ES_EXCHANGE, type = "topic"),
            key = ES_PRODUCT_DELETE_KEY
    ))
    public void deleteFromEs(String productId,
                             @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                             Channel channel) {
        boolean processedSuccessfully = false;

        try {
            elasticsearchClient.delete(req -> req
                    .index("product")
                    .id(productId)
            );

            processedSuccessfully = true;

        } catch (Exception e) {
            log.error("商品从ES删除失败", e);
        } finally {
            try {
                if (channel != null && channel.isOpen() && deliveryTag > 0) {
                    if (processedSuccessfully) {
                        channel.basicAck(deliveryTag, false);
                    } else {
                        channel.basicNack(deliveryTag, false, true);
                    }
                }
            } catch (IOException e) {
                log.error("删除消息确认失败", e);
            }
        }
    }

}
