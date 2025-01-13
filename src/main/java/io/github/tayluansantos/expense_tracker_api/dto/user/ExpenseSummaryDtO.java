package io.github.tayluansantos.expense_tracker_api.dto.user;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseSummaryDtO(Long id,
                                String description,
                                BigDecimal amount,
                                LocalDate createdAt,
                                CategoryResponseDto category){
}
