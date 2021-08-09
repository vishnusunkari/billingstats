package com.nic.billingstats.repository.p2.billing;

import com.nic.billingstats.repository.entity.p2.billing.BillingStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BillingStatsRepository extends CrudRepository<BillingStats, Integer> {

    /*@Query(value = "SELECT bs.id, " +
            "bs.brand_id, " +
            "bs.end_time, " +
            "bs.memberships_processed, " +
            "bs.memberships_expired, " +
            "bs.memberships_suspended, " +
            "bs.first_invoices_processed, " +
            "bs.recurring_invoices_processed " +
            "bs.recurring_invoices_paid " +
            "bs.discounts " +
            "bs.system_cancels " +
            "bs.superbad_cancels " +
            "bs.first_invoices_generated " +
            "bs.recurring_invoices_generated " +
            "FROM nic_billing.billing_stats bs WHERE bs.id = :id", nativeQuery = true)
    BillingStats getBillingStatsById(int id);*/

    BillingStats getBillingStatsById(Integer Id);

    @Override
    <S extends BillingStats> S save(S entity);
}
