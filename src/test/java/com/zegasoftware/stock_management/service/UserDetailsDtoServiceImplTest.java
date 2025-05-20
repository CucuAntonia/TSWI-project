package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.model.dto.user.UserDetailsDto;
import com.zegasoftware.stock_management.model.enums.UserRoles;
import com.zegasoftware.stock_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsDtoServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepo;

    private static final UserDetailsDto USER_DETAILS;

    static {
        USER_DETAILS = new UserDetailsDto("testuser", "encodedpassword", UserRoles.USER);
    }

    @Test
    void givenValidUsername_whenCallingLoadUserByUsername_thenReturnUserDetails() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(USER_DETAILS));
        org.springframework.security.core.userdetails.UserDetails userDetails =
                userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedpassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(userRepo, times(1)).findByUsername("testuser");
    }

    @Test
    void givenInvalidUsername_whenCallingLoadUserByUsername_thenThrowUsernameNotFoundException() {
        when(userRepo.findByUsername("invaliduser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("invaliduser"));
        verify(userRepo, times(1)).findByUsername("invaliduser");
    }

    @Test
    void givenValidUsernameWithNullRole_whenCallingLoadUserByUsername_thenReturnUserDetailsWithDefaultRole() {
        UserDetailsDto userWithNullRole = new UserDetailsDto("testuser", "encodedpassword", null);
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(userWithNullRole));

        org.springframework.security.core.userdetails.UserDetails userDetails =
                userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedpassword", userDetails.getPassword());
        assertFalse(userDetails.getAuthorities().isEmpty());
        verify(userRepo, times(1)).findByUsername("testuser");
    }
}
