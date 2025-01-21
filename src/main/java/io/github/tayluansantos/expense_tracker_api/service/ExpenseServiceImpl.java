package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.model.User;
import io.github.tayluansantos.expense_tracker_api.repository.CategoryRepository;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements IExpenseService{

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final IExpenseMapper expenseMapper;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              UserRepository userRepository,
                              IExpenseMapper expenseMapper,
                              CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.expenseMapper = expenseMapper;
        this.categoryRepository = categoryRepository;
    }

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
    public ExpenseResponseDto save(ExpenseRequestDto expenseRequest) {

        User user = userRepository.findById(expenseRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Category category = categoryRepository.findById(expenseRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        Expense expense = expenseMapper.convertToEntity(expenseRequest);

        expense.setUser(user);
        expense.setCategory(category);

        return expenseMapper.convertToDto(expenseRepository.save(expense));
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
