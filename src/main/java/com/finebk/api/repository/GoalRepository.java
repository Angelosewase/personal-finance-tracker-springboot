package com.finebk.api.repository;

import com.finebk.api.model.goal.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findByUserId(Long userId, Pageable pageable);
}