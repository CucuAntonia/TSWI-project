package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.mapper.UserMapper;
import com.zegasoftware.stock_management.model.dto.user.UserDetails;
import com.zegasoftware.stock_management.model.dto.user.UserSummary;
import com.zegasoftware.stock_management.model.entity.User;
import com.zegasoftware.stock_management.model.enums.UserRoles;
import com.zegasoftware.stock_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final User USER_ENTITY;
    private static final UserDetails USER_DETAILS;
    private static final UserSummary USER_SUMMARY;

    static {
        USER_ENTITY = new User();
        USER_ENTITY.setId(USER_ID);
        USER_ENTITY.setUsername("testuser");
        USER_ENTITY.setPassword("testpassword");
        USER_ENTITY.setRole(UserRoles.USER);
        USER_ENTITY.setDeleted(false);

        USER_DETAILS = new UserDetails("testuser", "testpassword", UserRoles.USER);

        USER_SUMMARY = new UserSummary("testuser", UserRoles.USER);
    }

    @Test
    void whenCallingGetUsers_thenReturnAllUsers() {
        List<UserSummary> summaries = List.of(USER_SUMMARY);
        when(userRepository.findAllUsers()).thenReturn(summaries);

        List<UserSummary> actualUsers = userService.getUsers();
        assertEquals(1, actualUsers.size());
        assertEquals(summaries, actualUsers);
    }

    @Test
    void givenValidId_whenCallingGetUserById_thenReturnOptionalUserSummary() {
        when(userRepository.findUserById(USER_ID)).thenReturn(Optional.of(USER_SUMMARY));

        Optional<UserSummary> userSummary = userService.getUserById(USER_ID);
        assertTrue(userSummary.isPresent());
        assertEquals(USER_SUMMARY, userSummary.get());
    }

    @Test
    void givenInvalidId_whenCallingGetUserById_thenReturnEmptyOptional() {
        when(userRepository.findUserById(USER_ID)).thenReturn(Optional.empty());

        Optional<UserSummary> userSummary = userService.getUserById(USER_ID);
        assertTrue(userSummary.isEmpty());
    }

    @Test
    void givenValidUsername_whenCallingFindUserByUsername_thenReturnOptionalUserSummary() {
        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(USER_SUMMARY));

        Optional<UserSummary> userSummary = userService.findUserByUsername("testuser");
        assertTrue(userSummary.isPresent());
        assertEquals(USER_SUMMARY, userSummary.get());
    }

    @Test
    void givenInvalidUsername_whenCallingFindUserByUsername_thenReturnEmptyOptional() {
        when(userRepository.findUserByUsername("invaliduser")).thenReturn(Optional.empty());

        Optional<UserSummary> userSummary = userService.findUserByUsername("invaliduser");
        assertTrue(userSummary.isEmpty());
    }

    @Test
    void givenValidUserDetails_whenCallingSaveUser_thenReturnTrue() {
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedpassword");
        when(userMapper.toEntity(USER_DETAILS)).thenReturn(USER_ENTITY);

        boolean result = userService.saveUser(USER_DETAILS);
        assertTrue(result);
        verify(userRepository, times(1)).save(USER_ENTITY);
    }

    @Test
    void givenNullUserDetails_whenCallingSaveUser_thenReturnFalse() {
        boolean result = userService.saveUser(null);
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidId_whenCallingDeleteUser_andNotDeleted_thenReturnTrue() {
        USER_ENTITY.setDeleted(false);
        USER_ENTITY.setPassword("testpassword");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER_ENTITY));

        boolean result = userService.deleteUser(USER_ID);
        assertTrue(result);
        assertTrue(USER_ENTITY.isDeleted());
        verify(userRepository, times(1)).save(USER_ENTITY);
    }


    @Test
    void givenValidId_whenCallingDeleteUser_andAlreadyDeleted_thenReturnFalse() {
        USER_ENTITY.setDeleted(true);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER_ENTITY));

        boolean result = userService.deleteUser(USER_ID);
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidId_whenCallingDeleteUser_thenReturnFalse() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(USER_ID);
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidUserWithNullPassword_whenCallingDeleteUser_thenThrowException() {
        USER_ENTITY.setDeleted(false);
        USER_ENTITY.setPassword(null);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER_ENTITY));

        assertThrows(IllegalStateException.class, () -> userService.deleteUser(USER_ID));
        verify(userRepository, never()).save(any());
    }
}
