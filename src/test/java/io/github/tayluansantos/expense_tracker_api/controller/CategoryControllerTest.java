package io.github.tayluansantos.expense_tracker_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Role;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import io.github.tayluansantos.expense_tracker_api.security.TokenService;
import io.github.tayluansantos.expense_tracker_api.service.ICategoryService;
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
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IUserService userService;

    @MockitoBean
    private ICategoryService categoryService;

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
        userModel.setRole(Role.ROLE_ADMIN);

        Mockito.when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userModel));
        Mockito.when(tokenService.validateToken(Mockito.anyString())).thenReturn(EMAIL);

        String mockToken = "mock-jwt-token";
        Mockito.when(tokenService.generateToken(Mockito.any(User.class))).thenReturn(mockToken);

        UserDetails userDetails = User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
                .build();

        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        CategoryResponseDto categoryResponse = new CategoryResponseDto(1L,"Category Test","Category description");
        Mockito.when(categoryService.save(Mockito.any(CategoryRequestDto.class))).thenReturn(categoryResponse);
   }

    @Test
    @DisplayName("findAll - Should return status code: 200 OK")
    void findAll() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(get("/categories")
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

        mockMvc.perform(get("/categories/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("save - Should return status code: 201 Created")
    void saveUser() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
                .build();

        String token = tokenService.generateToken(user);

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Category Test", "Category description");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(categoryRequestDto))
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Category Test"));
    }

    @Test
    @DisplayName("update - Should return status code: 200 OK")
    void updateUser() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
                .build();

        String token = tokenService.generateToken(user);

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Category Test - Updated","Category description");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L,"Category Test - Updated","Category description");

        Mockito.when(categoryService.update(Mockito.any(CategoryRequestDto.class),Mockito.anyLong())).thenReturn(categoryResponseDto);


        mockMvc.perform(put("/categories/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(categoryRequestDto))
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Category Test - Updated"));
    }

    @Test
    @DisplayName("delete - Should return status code: 204 No Content")
    void deleteUser() throws Exception {

        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(delete("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isNoContent());
    }


    private String convertJsonToString(CategoryRequestDto categoryRequestDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(categoryRequestDto);
    }
}