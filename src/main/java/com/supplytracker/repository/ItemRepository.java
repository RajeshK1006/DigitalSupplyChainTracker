package com.supplytracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.supplytracker.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{


}