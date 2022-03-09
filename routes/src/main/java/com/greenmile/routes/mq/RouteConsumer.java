package com.greenmile.routes.mq;

import com.greenmile.commons.vo.OrderVo;
import com.greenmile.routes.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RouteConsumer {

    @Autowired
    private RouteService service;

    @KafkaListener(topics = "${order.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(ConsumerRecord<String, OrderVo> consumerRecord) {
        log.info("Order: " + consumerRecord.value());
        service.alocateOrder(consumerRecord.value());
    }
}
