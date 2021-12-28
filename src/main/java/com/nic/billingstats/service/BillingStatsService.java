package com.nic.billingstats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.billingstats.model.BillingStatsEvent;
import com.nic.billingstats.model.BillingStatsMessage;
import com.nic.billingstats.repository.entity.p2.billing.BillingStats;
import com.nic.billingstats.repository.p2.billing.BillingStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author vishnu.sunkari
 * @created 07/30/2021
 */
@Slf4j
@Service
public class BillingStatsService {

    @Autowired
    BillingStatsRepository billingStatsRepository;

    @Transactional(value = "p2BillingTransactionManager")
    public void processBillingStats(List<String> messages, List<Integer> partitions, List<Long> offsets, Map<Integer, BillingStats> billingStatsMap) {
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
                        billingStats = aggregateBillingStats(billingStats, billingStatsMessage);
                        billingStatsMap.put(billingStatsEvent.getBillingStatsEventId(), billingStats);
                    }
                } catch (IOException e) {
                    log.error("BillingStatsService.processBillingStats IOException while consuming individual billingStatsMessage", e);
                }
            }
            saveBillingStats(billingStatsMap);
        } catch (Exception e) {
            log.error("BillingStatsService.processBillingStats Exception while consuming batch messages ", e);
        }
    }

    public void saveBillingStats(Map<Integer, BillingStats> billingStatsMap) {
        log.info("BillingStatsService.saveBillingStats Begin");
        for(Integer billingStatsEventId : billingStatsMap.keySet()) {
            BillingStats billingStatsCurrent = billingStatsMap.get(billingStatsEventId);
            log.debug("From billingStatsMap, billingStatsCurrent {} ", billingStatsCurrent);
            BillingStats billingStatsFromRepository = billingStatsRepository.getBillingStatsById(billingStatsEventId);
            log.debug("billingStatsFromRepository : {}", billingStatsFromRepository );
            billingStatsCurrent = billingStatsFromRepository.updateBillingStats(billingStatsCurrent);
            log.debug("Updated billingStatsCurrent : {}", billingStatsCurrent );
            billingStatsRepository.save(billingStatsCurrent);
            log.info("Updated BillingStats are saved to db");
        }
        log.info("BillingStatsService.saveBillingStats End");
    }

    public BillingStats aggregateBillingStats(BillingStats billingStats, BillingStatsMessage billingStatsMessage) {
        switch(billingStatsMessage.getBillingStatsName()) {
            case "membershipsProcessed": billingStats.increaseMembershipsProcessed(billingStatsMessage.getBillingStatsValue());break;
            case "membershipsExpired": billingStats.increaseMembershipsExpired(billingStatsMessage.getBillingStatsValue());break;
            case "membershipsSuspended": billingStats.increaseMembershipsSuspended(billingStatsMessage.getBillingStatsValue());break;
            case "firstInvoicesProcessed": billingStats.increaseFirstInvoicesProcessed(billingStatsMessage.getBillingStatsValue());break;
            case "firstInvoicesPaid": billingStats.increaseFirstInvoicesPaid(billingStatsMessage.getBillingStatsValue());break;
            case "recurringInvoicesProcessed": billingStats.increaseRecurringInvoicesProcessed(billingStatsMessage.getBillingStatsValue());break;
            case "recurringInvoicesPaid": billingStats.increaseRecurringInvoicesPaid(billingStatsMessage.getBillingStatsValue());break;
            case "discountsProvided": billingStats.increaseDiscountsProvided(billingStatsMessage.getBillingStatsValue());break;
            case "systemCancels": billingStats.increaseSystemCancels(billingStatsMessage.getBillingStatsValue());break;
            case "superbadCancels": billingStats.increaseSuperbadCancels(billingStatsMessage.getBillingStatsValue());break;
            case "firstInvoicesGenerated": billingStats.increaseFirstInvoicesGenerated(billingStatsMessage.getBillingStatsValue());break;
            case "recurringInvoicesGenerated": billingStats.increaseRecurringInvoicesGenerated(billingStatsMessage.getBillingStatsValue());break;
            default : log.error("No match for given billingStatsMessage name : " + billingStatsMessage.getBillingStatsName());
        }

        if (billingStatsMessage.isVerifyCalled()) {
            billingStats.increaseVerifyCallCount();
        }

        if (billingStatsMessage.getAmountCollected() != null) {
            billingStats.addAmountCollected(billingStatsMessage.getAmountCollected());
        }

        return billingStats;
    }
}
