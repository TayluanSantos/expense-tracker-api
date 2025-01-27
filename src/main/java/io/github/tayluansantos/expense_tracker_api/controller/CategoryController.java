package io.github.tayluansantos.expense_tracker_api.controller;

import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.category.CategoryResponseDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseRequestDto;
import io.github.tayluansantos.expense_tracker_api.dto.expense.ExpenseResponseDto;
import io.github.tayluansantos.expense_tracker_api.service.ICategoryService;
import io.github.tayluansantos.expense_tracker_api.service.IExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> save(@RequestBody @Valid CategoryRequestDto categoryRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(categoryRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(id));
    }


    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll () {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(@RequestBody CategoryRequestDto categoryRequest,
                                                     @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(categoryRequest,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        categoryService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
