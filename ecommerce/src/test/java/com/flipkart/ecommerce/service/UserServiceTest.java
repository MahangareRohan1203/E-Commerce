package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.config.JwtProvider;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtProvider jwtProvider;

    @InjectMocks
    UserServiceImplementation userService;

    @Test
    void testFindUserByIdSuccess() throws UserException {
        // Given
        User user = new User();
        // When
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        // Then
        User returnedUser = userService.findUserById(1L);
        assertEquals(user, returnedUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindUserByIdFailure() throws UserException {
        // Given
        given(userRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // When
        Throwable thrown = catchThrowable(() -> {
            userService.findUserById(1L);
        });
        // then
        assertThat(thrown).isInstanceOf(UserException.class).hasMessage("User not found");
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void findUserProfileByJwtSuccess() throws UserException {
        User user = new User();
        // Given
        given(jwtProvider.getEmailFromToken(any(String.class))).willReturn("test@gmail.com");
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));
        // When
        User response = userService.findUserProfileByJwt("lorem50");
        // Then
        assertEquals(user, response);
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtProvider, times(1)).getEmailFromToken(any(String.class));

    }

    @Test
    void findUserProfileByJwtFailure() {
        // Given
//        String email = "test@gmail.com";
//        given(jwtProvider.getEmailFromToken(any(String.class))).willReturn(email);
//        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
//        Throwable thrown = catchThrowable(() -> {
//            userService.findUserProfileByJwt(any(String.class));
//        });
//        // When
//        // Then
//        assertThat(thrown).isInstanceOf(UserException.class).hasMessage("User not found with email " + email);
//        verify(userRepository, times(1)).findByEmail(email);
//        verify(jwtProvider, times(1)).getEmailFromToken("lorem50");
    }
}