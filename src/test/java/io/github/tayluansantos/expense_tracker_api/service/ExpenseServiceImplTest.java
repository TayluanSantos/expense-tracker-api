package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.UserSummaryDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.CategoryRepository;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private IExpenseMapper expenseMapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense expense;
    private UserSummaryDto userSummaryDto;
    private CategoryResponseDto categoryResponseDto;
    private ExpenseResponseDto expenseResponseDto;
    private ExpenseRequestDto expenseRequestDto;
    private UserModel user;
    private Category category;

    @BeforeEach
    void setUp() {

        user = new UserModel();
        user.setId(1L);
        user.setName("User Test");

        userSummaryDto = new UserSummaryDto(
                user.getId(),
                user.getName()
        );

        category = new Category();
        category.setId(1L);
        category.setName("Category Test");
        category.setDescription("Category description");

        categoryResponseDto = new CategoryResponseDto(category.getId(),
                category.getName(),
                category.getDescription()
        );

        expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Test Expense");
        expense.setAmount(BigDecimal.valueOf(100.00));
        expense.setCreatedAt(LocalDate.now());
        expense.setUserModel(user);
        expense.setCategory(category);

        expenseResponseDto = new ExpenseResponseDto(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCreatedAt(),
                userSummaryDto,
                categoryResponseDto
        );

        expenseRequestDto = new ExpenseRequestDto(
                "Test Expense",
                BigDecimal.valueOf(100.00),
                user.getId(),
                category.getId()
        );
    }

    @Test
    @DisplayName("findAll - Should return list of expenses")
    void findAll_ShouldReturnExpensesList() {
        //given
        List<Expense> expenses = List.of(expense);
        List<ExpenseResponseDto> expectedDtos = List.of(expenseResponseDto);

        when(expenseRepository.findAll()).thenReturn(expenses);
        when(expenseMapper.convertListEntityToListDto(expenses)).thenReturn(expectedDtos);

        //when
        List<ExpenseResponseDto> result = expenseService.findAll();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(expenseRepository).findAll();
    }

    @Test
    @DisplayName("findAllByDateRange - Should return expenses within date range")
    void findAllByDateRange_ShouldReturnExpensesInDateRange() {
        //given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Expense> expenses = List.of(expense);
        List<ExpenseResponseDto> expectedDtos = List.of(expenseResponseDto);

        when(expenseRepository.findAllByDateRange(startDate, endDate)).thenReturn(expenses);
        when(expenseMapper.convertListEntityToListDto(expenses)).thenReturn(expectedDtos);

        //when
        List<ExpenseResponseDto> result = expenseService.findAllByDateRange(startDate, endDate);

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(expenseRepository).findAllByDateRange(startDate, endDate);
    }

    @Test
    @DisplayName("findById - Should return expense when found")
    void findById_ShouldReturnExpense() {
        //given
        when(expenseRepository.findById(anyLong())).thenReturn(Optional.of(expense));
        when(expenseMapper.convertToDto(expense)).thenReturn(expenseResponseDto);

        //when
        ExpenseResponseDto result = expenseService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(expenseResponseDto.id(), result.id());
        verify(expenseRepository).findById(1L);
    }

    @Test
    @DisplayName("findById - Should throw ResourceNotFoundException when expense not found")
    void findById_ShouldThrowResourceNotFoundException() {
        //given
        when(expenseRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThrows(ResourceNotFoundException.class, () -> expenseService.findById(1L));
    }

    @Test
    @DisplayName("save - Should create and return new expense")
    void save_ShouldCreateNewExpense() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(expenseMapper.convertToEntity(expenseRequestDto)).thenReturn(expense);
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.convertToDto(expense)).thenReturn(expenseResponseDto);

        //when
        ExpenseResponseDto result = expenseService.save(expenseRequestDto);

        //then
        assertNotNull(result);
        assertEquals(expenseResponseDto.id(), result.id());
        verify(userRepository).findById(user.getId());
        verify(categoryRepository).findById(category.getId());
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    @DisplayName("update - Should update existing expense")
    void update_ShouldUpdateExistingExpense() {
        //given
        when(expenseRepository.findById(anyLong())).thenReturn(Optional.of(expense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.convertToDto(expense)).thenReturn(expenseResponseDto);

        //when
        ExpenseResponseDto result = expenseService.update(expenseRequestDto, 1L);

        //then
        assertNotNull(result);
        assertEquals(expenseRequestDto.description(), expense.getDescription());
        assertEquals(expenseRequestDto.amount(), expense.getAmount());
        verify(expenseRepository).save(expense);
    }

    @Test
    @DisplayName("delete - Should delete existing expense")
    void delete_ShouldDeleteExistingExpense() {
        //given
        when(expenseRepository.findById(anyLong())).thenReturn(Optional.of(expense));
        doNothing().when(expenseRepository).delete(expense);

        //when
        assertDoesNotThrow(() -> expenseService.delete(1L));

        //then
        verify(expenseRepository).findById(1L);
        verify(expenseRepository).delete(expense);
    }

}