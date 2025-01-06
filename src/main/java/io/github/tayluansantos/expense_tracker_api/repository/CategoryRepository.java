package io.github.tayluansantos.expense_tracker_api.repository;

import io.github.tayluansantos.expense_tracker_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
