package io.github.tayluansantos.expense_tracker_api.dto.user;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Expense;

import java.util.List;

public record UserResponseDto(Long id, String name,List<UserExpenseDTO> expenses) { }
