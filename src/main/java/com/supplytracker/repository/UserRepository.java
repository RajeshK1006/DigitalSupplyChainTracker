package com.supplytracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.supplytracker.entity.User;

import java.util.Optional;


@Repository
public interface UserRepository  extends JpaRepository<User, Long>{

//	Optional<User> findByEmailIgnoreCase(String email);

	 Optional<User> findByEmail(String email);
}
