package com.finebk.api.payload;

import com.finebk.api.model.goal.GoalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class GoalRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Target amount must be greater than 0")
    private BigDecimal targetAmount;

    @NotNull
    @DecimalMin(value = "0.00", message = "Current amount cannot be negative")
    private BigDecimal currentAmount;

    @NotNull
    private Instant deadline;

    @Size(max = 255)
    private String description;

    @Size(max = 50)
    private String category;

    @NotNull
    private GoalStatus status;
}