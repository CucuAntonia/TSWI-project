package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.model.dto.user.UserDetails;
import com.zegasoftware.stock_management.model.dto.user.UserSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<UserSummary> getUsers();

    Optional<UserSummary> getUserById(UUID id);

    Optional<UserSummary> findUserByUsername(String username);

    boolean saveUser(UserDetails user);

    boolean deleteUser(UUID id);
}
