package com.finebk.api.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ExpenseRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull
    @Size(max = 50)
    private String category;

    @Size(max = 255)
    private String description;

    @NotNull
    private Instant date;
}