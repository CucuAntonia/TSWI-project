package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.mapper.UserMapper;
import com.zegasoftware.stock_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.zegasoftware.stock_management.model.dto.user.UserDetails> userDetails = userRepo.findByUsername(username);

        if (userDetails.isPresent()) {
            var user = userDetails.get();
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(String.valueOf(user.getRole()))
                    .build();
        }else {
            throw new UsernameNotFoundException(username);
        }
    }
}
