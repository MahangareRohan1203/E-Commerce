package com.flipkart.ecommerce.controller;


import com.flipkart.ecommerce.model.User;
import com.flipkart.ecommerce.service.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @MockBean
    UserServiceImplementation userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getUserProfileHandlerSuccess() throws Exception {
        User user = new User();
        String email = "test@gmail.com";
        user.setEmail(email);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile").accept(MediaType.APPLICATION_JSON).header("Authorization", "lorem50505"))
//                .andExpect(jsonPath("$.code").value(HttpStatus.OK))
//                .andExpect(jsonPath("$.email").value(email));
                .andExpect(status().isNotFound());

    }

    @Test
    void hello() {

    }
}