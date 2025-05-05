package com.finebk.api.payload;

import com.finebk.api.model.transaction.TransactionStatus;
import com.finebk.api.model.transaction.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionResponse extends DateAuditPayload {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private String category;
    private Instant date;
    private TransactionStatus status;
}