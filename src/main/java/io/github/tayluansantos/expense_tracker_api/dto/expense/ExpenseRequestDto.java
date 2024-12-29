package io.github.tayluansantos.expense_tracker_api.dto.expense;

import java.math.BigDecimal;

public record ExpenseRequestDto (String description,
                                BigDecimal amount){

}
