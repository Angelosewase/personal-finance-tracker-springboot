package com.finebk.api.service;

import com.finebk.api.exception.ResourceNotFoundException;
import com.finebk.api.model.goal.Goal;
import com.finebk.api.model.user.User;
import com.finebk.api.payload.GoalRequest;
import com.finebk.api.payload.GoalResponse;
import com.finebk.api.repository.GoalRepository;
import com.finebk.api.repository.UserRepository;
import com.finebk.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    public Goal createGoal(GoalRequest goalRequest, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Goal goal = new Goal(
                user,
                goalRequest.getTargetAmount(),
                goalRequest.getCurrentAmount(),
                goalRequest.getDeadline(),
                goalRequest.getDescription(),
                goalRequest.getCategory(),
                goalRequest.getStatus()
        );

        return goalRepository.save(goal);
    }

    public Goal updateGoal(Long goalId, GoalRequest goalRequest, UserPrincipal currentUser) {
        return goalRepository.findById(goalId)
                .map(goal -> {
                    if (!goal.getUser().getId().equals(currentUser.getId())) {
                        throw new ResourceNotFoundException("Goal", "id", goalId);
                    }

                    goal.setTargetAmount(goalRequest.getTargetAmount());
                    goal.setCurrentAmount(goalRequest.getCurrentAmount());
                    goal.setDeadline(goalRequest.getDeadline());
                    goal.setDescription(goalRequest.getDescription());
                    goal.setCategory(goalRequest.getCategory());
                    goal.setStatus(goalRequest.getStatus());

                    return goalRepository.save(goal);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
    }

    public Page<Goal> getGoalsByUserId(UserPrincipal currentUser, Pageable pageable) {
        return goalRepository.findByUserId(currentUser.getId(), pageable);
    }

    public Goal getGoalById(Long goalId, UserPrincipal currentUser) {
        return goalRepository.findById(goalId)
                .map(goal -> {
                    if (!goal.getUser().getId().equals(currentUser.getId())) {
                        throw new ResourceNotFoundException("Goal", "id", goalId);
                    }
                    return goal;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
    }

    public void deleteGoal(Long goalId, UserPrincipal currentUser) {
        goalRepository.findById(goalId)
                .map(goal -> {
                    if (!goal.getUser().getId().equals(currentUser.getId())) {
                        throw new ResourceNotFoundException("Goal", "id", goalId);
                    }
                    goalRepository.delete(goal);
                    return goal;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", goalId));
    }
}