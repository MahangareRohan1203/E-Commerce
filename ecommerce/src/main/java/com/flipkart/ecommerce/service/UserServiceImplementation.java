package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.config.JwtProvider;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        return userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);

        return userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found with email " + email));
    }
}
