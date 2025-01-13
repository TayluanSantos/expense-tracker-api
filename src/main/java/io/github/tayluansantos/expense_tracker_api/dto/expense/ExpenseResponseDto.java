package io.github.tayluansantos.expense_tracker_api.dto.expense;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDto (Long id,
                                  String description,
                                  BigDecimal amount,
                                  LocalDate createdAt,
                                  UserSummaryDto user,
                                  CategoryResponseDto category) {
}
