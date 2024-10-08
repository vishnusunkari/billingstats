package com.nic.billingstats.model;

/**
 * @author vishnu.sunkari
 * @created 07/12/2021
 */
public enum BillingStatsEnum {
    MEMBERSHIPS_PROCESSED("membershipsProcessed", 1),
    MEMBERSHIPS_EXPIRED("membershipsExpired", 1),
    MEMBERSHIPS_SUSPENDED("membershipsSuspended", 1),
    FIRST_INVOICES_PROCESSED("firstInvoicesProcessed", 1),
    FIRST_INVOICES_PAID("firstInvoicesPaid", 1),
    RECURRING_INVOICES_PROCESSED("recurringInvoicesProcessed", 1),
    RECURRING_INVOICES_PAID("recurringInvoicesPaid", 1),
    DISCOUNTS_PROVIDED("discountsProvided", 1),
    SYSTEM_CANCELS("systemCancels", 1),
    SUPERBAD_CANCELS("superbadCancels", 1),
    FIRST_INVOICES_GENERATED("firstInvoicesGenerated", 1),
    RECURRING_INVOICES_GENERATED("recurringInvoicesGenerated", 1),
    ;

    public String key;
    public int value;

    BillingStatsEnum(String key, int value){
        this.key = key;
        this.value = value;
    }

}
