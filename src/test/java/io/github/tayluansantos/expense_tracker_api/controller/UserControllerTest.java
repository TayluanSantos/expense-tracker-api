package io.github.tayluansantos.expense_tracker_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Role;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import io.github.tayluansantos.expense_tracker_api.security.TokenService;
import io.github.tayluansantos.expense_tracker_api.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUserService userService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    private final String EMAIL = "user123@gmail.com";
    private final String PASSWORD = "user1234";

    @BeforeEach
    void setUp() {

        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setName("User Test");
        userModel.setEmail(EMAIL);
        userModel.setPassword(PASSWORD);
        userModel.setRole(Role.ROLE_USER);

        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userModel));

        Mockito.when(tokenService.validateToken(Mockito.anyString())).thenReturn(EMAIL);

        String mockToken = "mock-jwt-token";
        Mockito.when(tokenService.generateToken(Mockito.any(User.class))).thenReturn(mockToken);

        UserDetails userDetails = User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        UserResponseDto userResponseDto = new UserResponseDto(1L, "User Test", null);
        Mockito.when(userService.findAll()).thenReturn(Collections.singletonList(userResponseDto));
        Mockito.when(userService.save(Mockito.any(UserRequestDto.class))).thenReturn(userResponseDto);
    }

    @Test
    @DisplayName("findAll - Should return status code: 200 OK")
    void findAll() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("findById - Should return status code: 200 OK")
    void findById() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(get("/users/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("findById - Should return status code: 201 Created")
    void saveUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("User Test", EMAIL, PASSWORD, Role.ROLE_USER);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("User Test"));
    }

    @Test
    @DisplayName("update - Should return status code: 200 OK")
    void updateUser() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        UserRequestDto userRequestDto = new UserRequestDto("User Test - Update", EMAIL, PASSWORD, Role.ROLE_USER);
        UserResponseDto userResponseDto = new UserResponseDto(1L,"User Test - Update", null);

        Mockito.when(userService.update(Mockito.any(UserRequestDto.class),Mockito.anyLong())).thenReturn(userResponseDto);


        mockMvc.perform(put("/users/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(userRequestDto))
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("User Test - Update"));
    }

    @Test
    @DisplayName("update - Should return status code: 204 No Content")
    void deleteUser() throws Exception {
        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        UserRequestDto userRequestDto = new UserRequestDto("User Test", EMAIL, PASSWORD, Role.ROLE_USER);

        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isNoContent());
    }


    private String convertJsonToString(UserRequestDto userRequestDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(userRequestDto);
    }
}