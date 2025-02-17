package io.github.tayluansantos.expense_tracker_api.dto.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExpenseRequestDto (@NotBlank String description,
                                 @NotNull BigDecimal amount,
                                 @NotNull Long categoryId,
                                 @NotNull Long userId){}
