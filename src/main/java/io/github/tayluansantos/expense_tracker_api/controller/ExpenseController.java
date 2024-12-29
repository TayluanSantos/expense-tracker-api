package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.model.Expense;
import io.github.tayluansantos.expense_tracker_api.service.IExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private IExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> save(@RequestBody @Valid ExpenseRequestDto expenseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.save(expenseRequest));
    }
}
