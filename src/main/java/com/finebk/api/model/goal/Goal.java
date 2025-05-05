package com.finebk.api.model.goal;

import com.finebk.api.model.audit.DateAudit;
import com.finebk.api.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "goals")
public class Goal extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "target_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal targetAmount;

    @NotNull
    @Column(name = "current_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentAmount;

    @NotNull
    @Column(name = "deadline", nullable = false)
    private Instant deadline;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 50)
    @Column(name = "category")
    private String category;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GoalStatus status;

    public Goal(User user, BigDecimal targetAmount, BigDecimal currentAmount, Instant deadline,
                String description, String category, GoalStatus status) {
        this.user = user;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
        this.description = description;
        this.category = category;
        this.status = status;
    }
}