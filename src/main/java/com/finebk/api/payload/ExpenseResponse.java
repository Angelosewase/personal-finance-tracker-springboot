package com.finebk.api.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpenseResponse extends DateAuditPayload {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String category;
    private String description;
    private Instant date;
}