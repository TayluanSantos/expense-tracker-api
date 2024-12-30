package io.github.tayluansantos.expense_tracker_api.mapper;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IExpenseMapper {

    ExpenseResponseDto convertToDto(Expense expense);
    Expense convertToEntity(ExpenseRequestDto expenseResponseDto);

    List<ExpenseResponseDto> convertListEntityToListDto(Iterable<Expense> expenses);

}
