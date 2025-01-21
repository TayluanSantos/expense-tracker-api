package io.github.tayluansantos.expense_tracker_api.repository;

import io.github.tayluansantos.expense_tracker_api.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    @Query("SELECT e FROM Expense e WHERE e.createdAt BETWEEN :startDate AND :endDate")
    List<Expense> findAllByDateRange(@Param(value = "startDate")LocalDate startDate,
                                     @Param(value = "endDate") LocalDate endDate);
}
