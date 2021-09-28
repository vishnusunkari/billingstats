package com.nic.billingstats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author vishnu.sunkari
 * @created 07/12/2021
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillingStatsMessage {

    private Integer brandId;
    private String billingStatsName;
    private Long billingStatsValue;
    private BigDecimal amountCollected;
    private boolean isVerifyCalled;
}
