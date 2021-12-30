package com.nic.billingstats.service.consumer;

import com.nic.billingstats.repository.entity.p2.billing.BillingStats;
import com.nic.billingstats.service.BillingStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vishnu.sunkari
 * @created 06/25/2021
 */
@Slf4j
@Component
public class BillingStatsConsumer {

    @Autowired
    BillingStatsService billingStatsService;

    @KafkaListener(topics = "#{'${kafka.topic.billingStatsTopic}'}")
    public void receive(@Payload List<String> messages,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets,
                        Acknowledgment acknowledgment) {
        Instant start = Instant.now();
        log.info("BillingStatsConsumer.receive Begin, batch messages size : " + messages.size());
        Map<Integer, BillingStats> billingStatsMap = new HashMap<>();
        acknowledgment.acknowledge();
        billingStatsService.processBillingStats(messages, partitions, offsets, billingStatsMap);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        log.info("End consume batch messages size: {}, Time taken in ms: {}", messages.size(), timeElapsed.toMillis());
    }

}
