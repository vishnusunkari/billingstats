package com.nic.billingstats.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.billingstats.model.BillingStatsEvent;
import com.nic.billingstats.model.BillingStatsMessage;
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

import java.io.IOException;
import java.util.ArrayList;
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

        log.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("BillingStatsConsumer.receive Begin, batch messages size : " + messages.size());
        Map<Integer, BillingStats> billingStatsMap = new HashMap<>();

        try {
            for (int i = 0; i < messages.size(); i++) {

                log.info("received message='{}' with partition-offset='{}'",
                        messages.get(i), partitions.get(i) + "-" + offsets.get(i));
                ObjectMapper mapper = new ObjectMapper();
                BillingStatsEvent billingStatsEvent = null;
                BillingStatsMessage billingStatsMessage = null;
                try {
                    billingStatsEvent = mapper.readValue(messages.get(i), BillingStatsEvent.class);
                    log.info("billingStatsEvent = " + billingStatsEvent.toString());
                    if (billingStatsEvent != null) {
                        billingStatsMessage = billingStatsEvent.getBillingStatsMessage();
                        BillingStats billingStats = billingStatsMap.getOrDefault(billingStatsEvent.getBillingStatsEventId(), new BillingStats());
                        billingStats = billingStatsService.aggregateBillingStats(billingStats, billingStatsMessage);
                        billingStatsMap.put(billingStatsEvent.getBillingStatsEventId(), billingStats);
                    }
                } catch (IOException e) {
                    log.error("BillingStatsConsumer.receive IOException while consuming individual billingStatsMessage", e);
                }
            }
            billingStatsService.processBillingStats(billingStatsMap);
        } catch(Exception e) {
            log.error("BillingStatsConsumer.receive Exception while consuming batch messages ", e);
        }
        /* Todo
         * Right now we are manually acknowledging offsets even if there is an exception
         *  so these messages wont get picked up again. But for this to happen one has to let
         *  the membership service run completely.
         */
        acknowledgment.acknowledge();
        log.info("BillingStatsConsumer.receive End, consume batch messages size : " + messages.size());
    }

    /*@Bean
    public KStream<String, String> billingStatsConsumerProcess(StreamsBuilder builder) {
        log.info("BillingStatsConsumer.billingStatsConsumerProcess Begin");
        //KStream<String, String> stream = builder.stream("BillingStatsTopic");
        var stringSerde = Serdes.String();
        //var billingStatsSerde = new BillingStatsEventSerde();
        var billingStatsSerde = new JsonSerde<>(BillingStatsEvent.class);
        //var billingStatsSerde = Serdes.String();

        KStream<String, BillingStatsEvent> billingStatsKTable = builder.stream("BillingStatsTopic", Consumed.with(stringSerde, billingStatsSerde))
                .map((k, v) -> KeyValue.pair(v.getBillingStatsEventId(), v))
                //.map((k, v) -> KeyValue.pair(""+v.getBrandId(), v.getBillingStatsValue()))
                //.peek((k, v) -> log.info("Key = " + k + " Value = " + v))
                //.groupByKey()
                //.toTable()
                .groupBy((k, v) -> KeyValue.pair(v.getBillingStatsName(), v)).count()

                ;
        log.info("billingStatsKTable.toString(): " + billingStatsKTable.toString());
                //.aggregate(() -> 0l, (aggKey, newValue, aggValue) -> aggValue + newValue, Materialized.with(stringSerde, Serdes.Long())).toStream()
                //.peek((k, v) -> log.info("Key = " + k + " Value = " + v));
                //.aggregate();
        log.info("BillingStatsConsumer.billingStatsConsumerProcess End");
        return billingStatsKTable;
        //return billingStatsKStream;
        //return 0l;
    }*/
}
