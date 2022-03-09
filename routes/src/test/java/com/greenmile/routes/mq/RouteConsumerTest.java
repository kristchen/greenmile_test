package com.greenmile.routes.mq;

import com.greenmile.commons.entity.Order;
import com.greenmile.commons.vo.OrderVo;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class RouteConsumerTest {

    @Autowired
    private KafkaConsumer consumer;


    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${order.topic}")
    private String topic;

//    @Test
//    public void sendOrder()throws Exception {
//
//        final String key = UUID.randomUUID().toString();
//        OrderVo vo = OrderVo.builder().id(38L).build();
//        ProducerRecord<String, OrderVo> pr = new ProducerRecord<>("ordertopic2", key, vo);
//        kafkaTemplate.send(pr);
//
//
//    }



}
