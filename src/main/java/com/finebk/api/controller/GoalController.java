package com.finebk.api.controller;

import com.finebk.api.model.goal.Goal;
import com.finebk.api.payload.ApiResponse;
import com.finebk.api.payload.GoalRequest;
import com.finebk.api.payload.GoalResponse;
import com.finebk.api.security.CurrentUser;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createGoal(@Valid @RequestBody GoalRequest goalRequest,
                                      @CurrentUser UserPrincipal currentUser) {
        Goal goal = goalService.createGoal(goalRequest, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{goalId}")
                .buildAndExpand(goal.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Goal created successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public Page<Goal> getGoals(@CurrentUser UserPrincipal currentUser,
                              Pageable pageable) {
        return goalService.getGoalsByUserId(currentUser, pageable);
    }

    @GetMapping("/{goalId}")
    @PreAuthorize("hasRole('USER')")
    public Goal getGoalById(@PathVariable Long goalId,
                           @CurrentUser UserPrincipal currentUser) {
        return goalService.getGoalById(goalId, currentUser);
    }

    @PutMapping("/{goalId}")
    @PreAuthorize("hasRole('USER')")
    public Goal updateGoal(@PathVariable Long goalId,
                          @Valid @RequestBody GoalRequest goalRequest,
                          @CurrentUser UserPrincipal currentUser) {
        return goalService.updateGoal(goalId, goalRequest, currentUser);
    }

    @DeleteMapping("/{goalId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteGoal(@PathVariable Long goalId,
                                      @CurrentUser UserPrincipal currentUser) {
        goalService.deleteGoal(goalId, currentUser);
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Goal deleted successfully"));
    }
}