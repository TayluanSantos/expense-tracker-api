package io.github.tayluansantos.expense_tracker_api.repository;


import io.github.tayluansantos.expense_tracker_api.model.*;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Expense expense;

    private UserModel user;

    private Category category;

    @BeforeEach
    void setup(){

        category = new Category(
                "Category Test",
                "Category description test"
        );
        categoryRepository.save(category);

        user = new UserModel(
                "User Test",
                "usertest@gmail.com",
                "user1234",
                Role.ROLE_USER
        );
        userRepository.save(user);

        expense = new Expense(
                "Expense description",
                BigDecimal.valueOf(100.00),
                LocalDate.now(),
                user,
                category
        );
    }

    @DisplayName("Return Expense when found by ID")
    @Test
    void givenExpenseId_WhenFindById_ThenReturnAExpense() {

        //Given
        Expense expenseSaved = expenseRepository.save(expense);
        Long expenseId = expenseSaved.getId();

        //When
        Optional<Expense> result = expenseRepository.findById(expenseId);

        //Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expenseSaved.getId(), result.get().getId());
    }

    @DisplayName("Return empty optional when Expense not found")
    @Test
    void givenInvalidId_WhenFindById_ThenReturnEmpty() {
        //Given
        Long invalidId = 999L;

        //When
        Optional<Expense> result = expenseRepository.findById(invalidId);

        //Then
        Assertions.assertTrue(result.isEmpty());
    }

    @DisplayName("Return list of all categories")
    @Test
    void whenFindAll_ThenReturnExpenseList() {

        //Given
        Expense savedUser = expenseRepository.save(expense);

        //When
        List<Expense> result = expenseRepository.findAll();

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
    }

    @DisplayName("Save and return Expense")
    @Test
    void givenExpenseObject_WhenSave_ThenReturnSavedExpense() {

        //When
        Expense result = expenseRepository.save(expense);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Expense description", result.getDescription());
    }

    @DisplayName("Update and return Expense")
    @Test
    void givenExpenseObject_WhenUpdate_ThenReturnUpdatedExpense() {

        //Given
        Expense savedExpense = expenseRepository.save(expense);
        savedExpense.setDescription("Expense description - Updated");

        //When
        Expense result = expenseRepository.save(savedExpense);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedExpense.getId(),result.getId());
        Assertions.assertEquals("Expense description - Updated", result.getDescription());

    }

    @DisplayName("Delete Expense by ID")
    @Test
    void givenExpenseId_WhenDelete_ThenReturnRemoveExpense() {

        //Given
        Expense expenseSaved = expenseRepository.save(expense);
        Long userId = expenseSaved.getId();

        //When
        expenseRepository.deleteById(userId);
        Optional<Expense> optionalExpense = expenseRepository.findById(userId);

        //Then
        Assertions.assertTrue(optionalExpense.isEmpty());
    }
}