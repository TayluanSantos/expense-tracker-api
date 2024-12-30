package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;

public interface IExpenseService {
    ExpenseResponseDto save(ExpenseRequestDto expenseRequestDto);
    ExpenseResponseDto update(ExpenseRequestDto expenseRequestDto,Long id);
}
