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

	// Verifies that only MANAGERs can perform the action
	private void authorizeManager(String email) {
		logger.info("Authorizing user with given email");

		User user = repo.findByEmail(email)
				.orElseThrow(() -> {
					logger.error("User not found for authorization");
					return new UserNotFoundException("User with this email is not found");
				});

		if (!"MANAGER".equalsIgnoreCase(String.valueOf(user.getRole()))) {
			logger.warn("Unauthorized access attempt by non-manager user");
			throw new InvalidRoleException("Only MANAGERs are authorized to perform this action");
		}

		logger.info("Authorization successful");
	}

	// Adds a new item; only accessible by MANAGERs
	@PostMapping
	public ItemDto addItem(@RequestParam String email, @Valid @RequestBody ItemDto itemDto) {
		logger.info("Request received to add item");
		authorizeManager(email);
		ItemDto addedItem = itemService.addItems(itemDto);
		logger.info("Item successfully added");
		return addedItem;
	}

	// Returns a list of all items
	@GetMapping
	public List<ItemDto> getAllItems() {
		logger.info("Fetching all items");
		return itemService.getAllItems();
	}

	// Returns item details by item ID
	@GetMapping("/{id}")
	public ItemDto getItemById(@PathVariable Long id) {
		logger.info("Fetching item by ID");
		return itemService.getById(id);
	}

	// Updates item information; only accessible by MANAGERs
	@PutMapping("/{id}")
	public ItemDto updateItem(@RequestParam String email, @PathVariable Long id, @Valid @RequestBody ItemDto itemDto) {
		logger.info("Request received to update item");
		authorizeManager(email);
		ItemDto updated = itemService.updateItem(id, itemDto);
		if (updated != null) {
			logger.info("Item update successful");
			return updated;
		}
		logger.error("Item update failed");
		throw new RuntimeException("Updation cannot be done for the item");
	}

	// Deletes an item by ID; only accessible by MANAGERs
	@DeleteMapping("/{id}")
	public String deleteItem(@RequestParam String email, @PathVariable Long id) {
		logger.info("Request received to delete item");
		authorizeManager(email);
		itemService.deleteItem(id);
		logger.info("Item deleted successfully");
		return "Item deleted successfully";
	}
}
