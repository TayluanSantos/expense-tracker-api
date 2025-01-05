package io.github.tayluansantos.expense_tracker_api.dto.expense;

import io.github.tayluansantos.expense_tracker_api.dto.user.UserResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDto (Long id,
                                  String description,
                                  BigDecimal amount,
                                  LocalDate createdAt,
                                  ExpenseUserDTO user) {
}
