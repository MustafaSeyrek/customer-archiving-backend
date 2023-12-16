package com.seyrek.customerarchiving.repositories;

import com.seyrek.customerarchiving.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
