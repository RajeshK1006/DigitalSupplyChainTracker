package com.supplytracker.controller;

import java.util.List;

import com.supplytracker.service.Imp.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.service.interfaces.ItemServiceInterface;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@PostMapping()
	public ItemDto addItem(@Valid @RequestBody ItemDto itemDto){
		return itemService.addItems(itemDto);
	}
	
	@GetMapping()
	public List<ItemDto> getAllItems(){
		return itemService.getAllItems();
			
	}
	
	@GetMapping("/{id}")
	public ItemDto getItemById(@PathVariable Long id){
		return itemService.getById(id);
	}
	
	@PutMapping("/{id}")
	public ItemDto updateItem(@PathVariable Long id, @Valid @RequestBody ItemDto itemDto){
		ItemDto updated = itemService.updateItem(id, itemDto);
		if(updated!=null) return updated;
		throw new RuntimeException("Updation cant be done for the Item");
	}
	
	@DeleteMapping("/{id}")
	public String deleteItem(@PathVariable Long id){
		itemService.deleteItem(id);
		return "Item deleted successfully";
	}
}
