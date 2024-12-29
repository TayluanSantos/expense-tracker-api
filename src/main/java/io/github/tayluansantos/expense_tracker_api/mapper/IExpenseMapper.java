package io.github.tayluansantos.expense_tracker_api.mapper;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IExpenseMapper {

    ExpenseResponseDto convertToDto(Expense expense);
    Expense convertToEntity(ExpenseRequestDto expenseResponseDto);
}
