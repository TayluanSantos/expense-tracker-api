package io.github.tayluansantos.expense_tracker_api.dto.user;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserRequestDto(@NotNull String name,
                             @NotNull @Email String email,
                             @NotNull @Size(min = 8) String password                            )

{ }
