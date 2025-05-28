package com.supplytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supplytracker.entity.User;


@Repository
public interface UserRepository  extends JpaRepository<User, Long>{

	User findByEmailIgnoreCase(String email);
}
