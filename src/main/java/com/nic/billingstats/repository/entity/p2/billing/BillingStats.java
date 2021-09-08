package com.nic.billingstats.repository.entity.p2.billing;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "billing_stats", catalog = "nic_billing")
@Data
public class BillingStats implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "brand_id")
    private int brandId;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "memberships_processed")
    private int membershipsProcessed;

    @Column(name = "memberships_expired")
    private int membershipsExpired;

    @Column(name = "memberships_suspended")
    private int membershipsSuspended;

    @Column(name = "first_invoices_processed")
    private int firstInvoicesProcessed;

    @Column(name = "first_invoices_paid")
    private int firstInvoicesPaid;

    @Column(name = "recurring_invoices_processed")
    private int recurringInvoicesProcessed;

    @Column(name = "recurring_invoices_paid")
    private int recurringInvoicesPaid;

    @Column(name = "discounts")
    private int discountsProvided;

    @Column(name = "system_cancels")
    private int systemCancels;

    @Column(name = "superbad_cancels")
    private int superbadCancels;

    @Column(name = "first_invoices_generated")
    private int firstInvoicesGenerated;

    @Column(name = "recurring_invoices_generated")
    private int recurringInvoicesGenerated;

    @Column(name = "verify_call_count")
    private int verifyCallCount;

    @Column(name = "amount_collected")
    private BigDecimal amountCollected;

    public void increaseMembershipsProcessed(Long valueToIncrement) {
        membershipsProcessed+=valueToIncrement;
    }

    public void increaseMembershipsExpired(Long valueToIncrement) {
        this.membershipsExpired+=valueToIncrement;
    }

    public void increaseMembershipsSuspended(Long valueToIncrement) {
        this.membershipsSuspended+=valueToIncrement;
    }

    public void increaseDiscountsProvided(Long valueToIncrement) {
        this.discountsProvided+=valueToIncrement;;
    }

    public void increaseSystemCancels(Long valueToIncrement) {
        this.systemCancels+=valueToIncrement;;
    }

    public void increaseFirstInvoicesProcessed(Long valueToIncrement) {
        this.firstInvoicesProcessed+=valueToIncrement;;
    }

    public void increaseRecurringInvoicesProcessed(Long valueToIncrement) {
        this.recurringInvoicesProcessed+=valueToIncrement;;
    }

    public void increaseFirstInvoicesPaid(Long valueToIncrement) {
        this.firstInvoicesPaid+=valueToIncrement;;
    }

    public void increaseRecurringInvoicesPaid(Long valueToIncrement) {
        this.recurringInvoicesPaid+=valueToIncrement;;
    }

    public void increaseSuperbadCancels(Long valueToIncrement) {
        this.superbadCancels+=valueToIncrement;;
    }

    public void increaseFirstInvoicesGenerated(Long valueToIncrement) {
        this.firstInvoicesGenerated+=valueToIncrement;;
    }

    public void increaseRecurringInvoicesGenerated(Long valueToIncrement) {
        this.recurringInvoicesGenerated+=valueToIncrement;;
    }

    public void addAmountCollected(BigDecimal amountCollected) {
        this.amountCollected = this.amountCollected != null ? this.amountCollected.add(amountCollected) : amountCollected;
    }

    public void increaseVerifyCallCount() {
        this.verifyCallCount = this.verifyCallCount + 1;
    }

    public BillingStats updateBillingStats(BillingStats billingStatsCurrent) {
        this.membershipsProcessed += billingStatsCurrent.getMembershipsProcessed();
        this.membershipsExpired += billingStatsCurrent.getMembershipsExpired();
        this.membershipsSuspended += billingStatsCurrent.getMembershipsSuspended();
        this.firstInvoicesProcessed += billingStatsCurrent.getFirstInvoicesProcessed();
        this.firstInvoicesPaid += billingStatsCurrent.getFirstInvoicesPaid();
        this.recurringInvoicesProcessed += billingStatsCurrent.getRecurringInvoicesProcessed();
        this.recurringInvoicesPaid += billingStatsCurrent.getRecurringInvoicesPaid();
        this.discountsProvided += billingStatsCurrent.getDiscountsProvided();
        this.systemCancels += billingStatsCurrent.getSystemCancels();
        this.superbadCancels += billingStatsCurrent.getSuperbadCancels();
        this.firstInvoicesGenerated += billingStatsCurrent.getFirstInvoicesGenerated();
        this.recurringInvoicesGenerated += billingStatsCurrent.getRecurringInvoicesGenerated();
        this.amountCollected = this.amountCollected.add(billingStatsCurrent.getAmountCollected() != null ? billingStatsCurrent.getAmountCollected() : new BigDecimal(0));
        this.verifyCallCount += billingStatsCurrent.getVerifyCallCount();
        return this;
    }
}
