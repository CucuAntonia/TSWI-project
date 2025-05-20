package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.model.dto.user.UserDetailsDto;
import com.zegasoftware.stock_management.model.dto.user.UserSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserSummary> getUsers();

    Optional<UserSummary> getUserById(UUID id);

    Optional<UserSummary> findUserByUsername(String username);

    boolean saveUser(UserDetailsDto user);

    boolean deleteUser(UUID id);
}
