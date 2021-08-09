package com.nic.billingstats.service;

import com.nic.billingstats.model.BillingStatsMessage;
import com.nic.billingstats.repository.entity.p2.billing.BillingStats;
import com.nic.billingstats.repository.p2.billing.BillingStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void processBillingStats(Map<Integer, BillingStats> billingStatsMap) {
        log.info("BillingStatsService.processBillingStats Begin");
        for(Integer billingStatsEventId : billingStatsMap.keySet()) {
            BillingStats billingStatsCurrent = billingStatsMap.get(billingStatsEventId);
            log.info("From billingStatsMap, billingStatsCurrent {} ", billingStatsCurrent);
            BillingStats billingStatsFromRepository = billingStatsRepository.getBillingStatsById(billingStatsEventId);
            log.info("billingStatsFromRepository : {}", billingStatsFromRepository );
            billingStatsCurrent = billingStatsFromRepository.updateBillingStats(billingStatsCurrent);
            log.info("Updated billingStatsCurrent : {}", billingStatsCurrent );
            billingStatsRepository.save(billingStatsCurrent);
            log.info("Updated BillingStats are saved to db");
            //billingStats.setMembershipsProcessed();
        }
        log.info("BillingStatsService.processBillingStats End");
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
            default : log.info("No match for given billingStatsMessage name : " + billingStatsMessage.getBillingStatsName());
        }
        return billingStats;
    }
}
