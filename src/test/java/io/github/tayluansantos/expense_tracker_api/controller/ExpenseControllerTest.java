package io.github.tayluansantos.expense_tracker_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Role;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import io.github.tayluansantos.expense_tracker_api.security.TokenService;
import io.github.tayluansantos.expense_tracker_api.service.IExpenseService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IExpenseService expenseService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    private final String EMAIL = "user123@gmail.com";
    private final String PASSWORD = "user1234";
    private ExpenseResponseDto expenseResponseDto;
    private ExpenseRequestDto expenseRequestDto;

    @BeforeEach
    void setUp() {
        // Configurando usuário para autenticação
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

        // Configurando dados de despesa para os testes
        expenseResponseDto = new ExpenseResponseDto(
                1L,
                "Aluguel",
                new BigDecimal("1500.00"),
                LocalDate.now(),
                null,
                null
        );

        expenseRequestDto = new ExpenseRequestDto(
                "Aluguel",
                new BigDecimal("1500.00"),
                1L,
                1L
        );

        // Mock para os métodos do serviço de despesas
        Mockito.when(expenseService.findAll()).thenReturn(Collections.singletonList(expenseResponseDto));
        Mockito.when(expenseService.findById(1L)).thenReturn(expenseResponseDto);
        Mockito.when(expenseService.save(Mockito.any(ExpenseRequestDto.class))).thenReturn(expenseResponseDto);
        Mockito.when(expenseService.update(Mockito.any(ExpenseRequestDto.class), Mockito.anyLong())).thenReturn(expenseResponseDto);
        Mockito.when(expenseService.findAllByDateRange(Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(Collections.singletonList(expenseResponseDto));
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

        mockMvc.perform(get("/expenses")
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

        mockMvc.perform(get("/expenses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("findByDate - Should return status code: 200 OK")
    void findByDate() throws Exception {
        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(get("/expenses/search-by-date")
                        .param("startDate", LocalDate.now().minusDays(30).toString())
                        .param("endDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("save - Should return status code: 201 Created")
    void saveExpense() throws Exception {
        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(expenseRequestDto))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Aluguel"));
    }

    @Test
    @DisplayName("update - Should return status code: 200 OK")
    void updateExpense() throws Exception {
        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        ExpenseRequestDto updatedExpenseRequestDto = new ExpenseRequestDto(
                "Aluguel Atualizado",
                new BigDecimal("1600.00"),
                1L,
                1L
        );

        ExpenseResponseDto updatedExpenseResponseDto = new ExpenseResponseDto(
                1L,
                "Aluguel Atualizado",
                new BigDecimal("1600.00"),
                LocalDate.now(),
                null,
                null
        );

        Mockito.when(expenseService.update(Mockito.any(ExpenseRequestDto.class), Mockito.anyLong()))
                .thenReturn(updatedExpenseResponseDto);

        mockMvc.perform(put("/expenses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(updatedExpenseRequestDto))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Aluguel Atualizado"));
    }

    @Test
    @DisplayName("delete - Should return status code: 204 No Content")
    void deleteExpense() throws Exception {
        User user = (User) User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(Role.ROLE_USER.name()))
                .build();

        String token = tokenService.generateToken(user);

        mockMvc.perform(delete("/expenses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    private String convertJsonToString(ExpenseRequestDto expenseRequestDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(expenseRequestDto);
    }
}