package com.finebk.api.service;

import com.finebk.api.model.user.User;
import com.finebk.api.security.UserPrincipal;
import com.finebk.api.payload.ApiResponse;
import com.finebk.api.payload.InfoRequest;
import com.finebk.api.payload.UserIdentityAvailability;
import com.finebk.api.payload.UserProfile;
import com.finebk.api.payload.UserSummary;

public interface UserService {

	UserSummary getCurrentUser(UserPrincipal currentUser);

	UserIdentityAvailability checkUsernameAvailability(String username);

	UserIdentityAvailability checkEmailAvailability(String email);

	UserProfile getUserProfile(String username);

	User addUser(User user);

	User updateUser(User newUser, String username, UserPrincipal currentUser);

	ApiResponse deleteUser(String username, UserPrincipal currentUser);

	ApiResponse giveAdmin(String username);

	ApiResponse removeAdmin(String username);

	UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);

}