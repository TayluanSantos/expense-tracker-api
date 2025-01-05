package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.model.User;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements IExpenseService{

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IExpenseMapper expenseMapper;

    @Override
    public List<ExpenseResponseDto> findAll() {
        return expenseMapper.convertListEntityToListDto(expenseRepository.findAll());
    }

    @Override
    public ExpenseResponseDto findById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found expense with id: " + id));
        return expenseMapper.convertToDto(expense);
    }

    @Override
    public ExpenseResponseDto save(Long id, ExpenseRequestDto expenseRequest) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Expense expense = expenseMapper.convertToEntity(expenseRequest);
        expense.setUser(user);

        expenseRepository.save(expense);

        return expenseMapper.convertToDto(expense);
    }

    @Override
    public ExpenseResponseDto update(ExpenseRequestDto expenseRequestDto, Long id) {
        Expense expense = expenseRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Cannot found expense with id: " + id));
        expense.setDescription(expenseRequestDto.description());
        expense.setAmount(expenseRequestDto.amount());
        expenseRepository.save(expense);

        return expenseMapper.convertToDto(expense);
    }

    @Override
    public void delete(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot found expense with id: " + id));
        expenseRepository.delete(expense);
    }
}
