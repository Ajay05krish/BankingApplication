package com.user.bankUserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.bankUserService.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
