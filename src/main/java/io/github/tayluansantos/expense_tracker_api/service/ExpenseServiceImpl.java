package io.github.tayluansantos.expense_tracker_api.service;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.exception.ResourceNotFoundException;
import io.github.tayluansantos.expense_tracker_api.mapper.IExpenseMapper;
import io.github.tayluansantos.expense_tracker_api.model.Category;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.model.UserModel;
import io.github.tayluansantos.expense_tracker_api.repository.CategoryRepository;
import io.github.tayluansantos.expense_tracker_api.repository.ExpenseRepository;
import io.github.tayluansantos.expense_tracker_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseServiceImpl implements IExpenseService{

    private static Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);

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

        logger.info("Retrieving all expenses");
        return expenseMapper.convertListEntityToListDto(expenseRepository.findAll());
    }

    @Override
    public List<ExpenseResponseDto> findAllByDateRange(LocalDate startDate, LocalDate endDate) {

        logger.info("Attempting to retrieve expenses by date");
        List<Expense> expenseList =
                expenseRepository.findAllByDateRange(startDate, endDate);

        List<ExpenseResponseDto> expenseResponseDtos = expenseMapper.convertListEntityToListDto(expenseList);
        logger.info("Retrieving all expenses by date succsessfully");

        return expenseResponseDtos;
    }

    @Override
    public ExpenseResponseDto findById(Long id) {

        logger.info("Attempting to find expense with id:{}",id);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Error: Expense with id {} not found", id);
                    return new ResourceNotFoundException("Cannot found expense with id: " + id);
                });

        ExpenseResponseDto expenseResponseDto = expenseMapper.convertToDto(expense);
        logger.info("Expense with id {} was found succsessfully",id);

        return expenseResponseDto;
    }

    @Override
    public ExpenseResponseDto save(ExpenseRequestDto expenseRequest) {

        logger.info("Attempting to create expense");

        UserModel userModel = userRepository.findById(expenseRequest.userId())
                .orElseThrow(() -> {
                    logger.warn("Error: User with id {} not found",expenseRequest.userId());
                    return new ResourceNotFoundException("User not found.");
                });

        Category category = categoryRepository.findById(expenseRequest.categoryId())
                .orElseThrow(() -> {
                    logger.warn("Error: Category with id {} not found",expenseRequest.categoryId());
                    return new ResourceNotFoundException("Category not found.");
                });

        Expense expense = expenseMapper.convertToEntity(expenseRequest);

        expense.setUserModel(userModel);
        expense.setCategory(category);

        Expense persistedExpense = expenseRepository.save(expense);
        logger.info("Expense created successfully");

        return expenseMapper.convertToDto(persistedExpense);
    }

    @Override
    public ExpenseResponseDto update(ExpenseRequestDto expenseRequestDto, Long id) {

        logger.info("Attempting to update expense with id:{}",id);

        Expense expense = expenseRepository
                .findById(id).orElseThrow(() -> {
                    logger.warn("Error: Expense with id {} was not found",id);
                    return new ResourceNotFoundException("Cannot found expense with id: " + id);
                });

        expense.setDescription(expenseRequestDto.description());
        expense.setAmount(expenseRequestDto.amount());
        expenseRepository.save(expense);

        logger.info("Expense with id{} was updated successfully",id);

        return expenseMapper.convertToDto(expense);
    }

    @Override
    public void delete(Long id) {

        logger.info("Attempting to delete expense with id:{}",id);

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Error: Expense with id {} not found",id);
                    return new ResourceNotFoundException("Cannot found expense with id: " + id);
                });
        expenseRepository.delete(expense);

        logger.info("Expense with id {} was deleted succsessfully",id);

    }
}
