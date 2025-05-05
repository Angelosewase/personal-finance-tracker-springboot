package com.finebk.api.payload;

import com.finebk.api.model.goal.GoalStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class GoalResponse extends DateAuditPayload {
    private Long id;
    private Long userId;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private Instant deadline;
    private String description;
    private String category;
    private GoalStatus status;
}