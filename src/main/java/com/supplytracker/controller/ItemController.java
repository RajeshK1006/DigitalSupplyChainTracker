package com.supplytracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@PostMapping
	public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto itemDto){
		ItemDto createdItem = itemService.addItems(itemDto);
		return ResponseEntity.ok(createdItem);
	}
	
	@GetMapping
	public ResponseEntity<List<ItemDto>> getAllItems(){
		List<ItemDto> items = itemService.getAllItems();
		return ResponseEntity.ok(items);		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ItemDto> getItemById(@PathVariable Long id){
		ItemDto item = itemService.getById(id);
		return ResponseEntity.ok(item);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @Valid @RequestBody ItemDto itemDto){
		ItemDto updated = itemService.updateItem(id, itemDto);
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteItem(@PathVariable Long id){
		itemService.deleteItem(id);
		return ResponseEntity.ok("Item deleted successfully");
	}
}
