package com.finebk.api.payload;

import com.finebk.api.model.transaction.TransactionStatus;
import com.finebk.api.model.transaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class TransactionRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @Size(max = 255)
    private String description;

    @Size(max = 50)
    private String category;

    @NotNull
    private Instant date;

    @NotNull
    private TransactionStatus status;
}