package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.mapper.IUserMapper;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements IExpenseService{

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IExpenseMapper expenseMapper;

    @Override
    public ExpenseResponseDto save(ExpenseRequestDto expenseRequest) {
        Expense expense = expenseMapper.convertToEntity(expenseRequest);
        expenseRepository.save(expense);

        return expenseMapper.convertToDto(expense);
    }
}
