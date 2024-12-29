package io.github.tayluansantos.expense_tracker_api.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDto (Long id,
                                  String description,
                                  BigDecimal amount,
                                  LocalDate createdAt) {
}
