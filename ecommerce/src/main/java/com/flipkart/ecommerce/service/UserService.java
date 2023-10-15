package com.flipkart.ecommerce.service;

import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.model.User;
import org.springframework.stereotype.Service;

public interface UserService {
    public User findUserById(Long userId) throws UserException;
    public User findUserProfileByJwt(String jwt) throws UserException;

}
