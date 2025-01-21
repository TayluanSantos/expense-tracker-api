package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.service.ICategoryService;
import io.github.tayluansantos.expense_tracker_api.service.IExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final IExpenseService expenseService;

    public ExpenseController(IExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> save(@RequestBody @Valid ExpenseRequestDto expenseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.save(expenseRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findById(id));
    }


    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> findAll () {
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> update(@RequestBody ExpenseRequestDto expenseRequest,
                                                     @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(expenseService.update(expenseRequest,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        expenseService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
