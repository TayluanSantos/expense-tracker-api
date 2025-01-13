package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;

import java.util.List;

public interface IExpenseService {
    List<ExpenseResponseDto> findAll();
    ExpenseResponseDto findById(Long id);
    ExpenseResponseDto save(ExpenseRequestDto expenseRequestDto);
    ExpenseResponseDto update(ExpenseRequestDto expenseRequestDto,Long id);
    void delete(Long id);
}
