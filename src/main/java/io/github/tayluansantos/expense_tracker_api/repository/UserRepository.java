package io.github.tayluansantos.expense_tracker_api.repository;

import io.github.tayluansantos.expense_tracker_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByEmail(String email);
}
