package io.github.tayluansantos.expense_tracker_api.dto.user;

import java.util.List;

public record UserResponseDto(Long id, String name,List<ExpenseSummaryDtO> expenses) { }
