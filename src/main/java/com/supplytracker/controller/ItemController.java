package com.supplytracker.controller;

import java.util.List;

import com.supplytracker.entity.User;
import com.supplytracker.exception.InvalidRoleException;
import com.supplytracker.exception.UserNotFoundException;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.supplytracker.dto.ItemDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserRepository repo;

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	private void authorizeManager(String email) {
		logger.info("Authorizing user with email: {}", email);
		User user = repo.findByEmail(email)
				.orElseThrow(() -> {
					logger.error("User not found with email: {}", email);
					return new UserNotFoundException("User with this email is not found");
				});

		if (!"MANAGER".equalsIgnoreCase(String.valueOf(user.getRole()))) {
			logger.warn("Unauthorized access attempt by user with email: {}", email);
			throw new InvalidRoleException("Only MANAGERs are authorized to perform this action");
		}
		logger.info("Authorization successful for user: {}", email);
	}

	@PostMapping
	public ItemDto addItem(@RequestParam String email, @Valid @RequestBody ItemDto itemDto) {
		logger.info("Request to add item by user: {}", email);
		authorizeManager(email);
		ItemDto addedItem = itemService.addItems(itemDto);
		logger.info("Item added successfully: {}", addedItem.getName());
		return addedItem;
	}

	@GetMapping
	public List<ItemDto> getAllItems() {
		logger.info("Fetching all items");
		return itemService.getAllItems();
	}

	@GetMapping("/{id}")
	public ItemDto getItemById(@PathVariable Long id) {
		logger.info("Fetching item with ID: {}", id);
		return itemService.getById(id);
	}

	@PutMapping("/{id}")
	public ItemDto updateItem(@RequestParam String email, @PathVariable Long id, @Valid @RequestBody ItemDto itemDto) {
		logger.info("Request to update item ID: {} by user: {}", id, email);
		authorizeManager(email);
		ItemDto updated = itemService.updateItem(id, itemDto);
		if (updated != null) {
			logger.info("Item updated successfully: {}", updated.getName());
			return updated;
		}
		logger.error("Failed to update item with ID: {}", id);
		throw new RuntimeException("Updation cannot be done for the item");
	}

	@DeleteMapping("/{id}")
	public String deleteItem(@RequestParam String email, @PathVariable Long id) {
		logger.info("Request to delete item ID: {} by user: {}", id, email);
		authorizeManager(email);
		itemService.deleteItem(id);
		logger.info("Item deleted successfully with ID: {}", id);
		return "Item deleted successfully";
	}
}
