package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import org.springframework.beans.BeanUtils;
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

    @Override
    public ExpenseResponseDto update(ExpenseRequestDto expenseRequestDto, Long id) {
        Expense expense = expenseRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Expense not found."));
        BeanUtils.copyProperties(expenseRequestDto,expense);
        expenseRepository.save(expense);

        return expenseMapper.convertToDto(expense);
    }
}
