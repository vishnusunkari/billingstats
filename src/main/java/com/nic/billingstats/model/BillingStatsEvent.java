package com.nic.billingstats.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author vishnu.sunkari
 * @created 07/12/2021
 */

@Data
public class BillingStatsEvent {
    private Integer billingStatsEventId;

    @NotNull
    @Valid
    private BillingStatsMessage billingStatsMessage;
}
