package com.flipkart.ecommerce.controller;

import com.flipkart.ecommerce.Exceptions.UserException;
import com.flipkart.ecommerce.config.JwtProvider;
import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.repository.UserRepository;
import com.flipkart.ecommerce.request.LoginRequest;
import com.flipkart.ecommerce.response.AuthResponse;
import com.flipkart.ecommerce.service.CartServiceImplementation;
import com.flipkart.ecommerce.service.CustomUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserServiceImplementation customUserServiceImplementation;

    @Autowired
    private CartServiceImplementation cartService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("User Already Exists with email " + email);
        }
        String password = passwordEncoder.encode(user.getPassword());
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(password);
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setRole("USER");
        User savedUser = userRepository.save(createdUser);
        cartService.createCart(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = jwtProvider.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponse("Sign In To get JWT", "Signup Success"), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticate(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        String token = jwtProvider.generateToken(authentication, user.getRole());

        return new ResponseEntity<>(new AuthResponse(token, "Signin Successful"), HttpStatus.ACCEPTED);
    }


    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
        if (userDetails == null) throw new BadCredentialsException("Invalid username");

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password ");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
