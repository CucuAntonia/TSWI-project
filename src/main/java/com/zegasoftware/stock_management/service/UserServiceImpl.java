package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.mapper.UserMapper;
import com.zegasoftware.stock_management.model.dto.user.UserDetailsDto;
import com.zegasoftware.stock_management.model.dto.user.UserSummary;
import com.zegasoftware.stock_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserSummary> getUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public Optional<UserSummary> getUserById(UUID id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Optional<UserSummary> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean saveUser(UserDetailsDto user) {
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userMapper.toEntity(user));
        return true;
    }

    @Override
    public boolean deleteUser(UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.isDeleted()) {
                        user.setDeleted(true);
                        if (user.getPassword() == null) {
                            throw new IllegalStateException("Password cannot be null during update.");
                        }
                        user.setId(id);
                        userRepository.save(user);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
}
