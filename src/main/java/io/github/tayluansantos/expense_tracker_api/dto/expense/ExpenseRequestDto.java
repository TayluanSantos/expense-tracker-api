package io.github.tayluansantos.expense_tracker_api.dto.expense;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExpenseRequestDto (@NotNull String description,
                                @NotNull BigDecimal amount){

}
