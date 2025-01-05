package io.github.tayluansantos.expense_tracker_api.dto.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserExpenseDTO (Long id,
                              String description,
                              BigDecimal amount,
                              LocalDate createdAt){
}
