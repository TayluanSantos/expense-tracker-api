package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements IExpenseService{

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IExpenseMapper expenseMapper;

    @Override
    public List<ExpenseResponseDto> findAll() {
        return expenseMapper.convertListEntityToListDto(expenseRepository.findAll());
    }

    @Override
    public ExpenseResponseDto findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot found expense with id: " + id));
        return expenseMapper.convertToDto(expense);
    }

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
        expense.setDescription(expense.getDescription());
        expense.setAmount(expense.getAmount());

        expenseRepository.save(expense);

        return expenseMapper.convertToDto(expense);
    }

    @Override
    public void delete(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot found expense with id: " + id));
        expenseRepository.delete(expense);
    }
}
