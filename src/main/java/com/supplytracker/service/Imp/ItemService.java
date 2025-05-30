package com.supplytracker.service.Imp;

import java.util.ArrayList;
import java.util.List;

import com.supplytracker.service.interfaces.ItemServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.entity.Item;
import com.supplytracker.entity.User;
import com.supplytracker.exception.ItemNotFoundException;
import com.supplytracker.exception.ResourceNotFoundException;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ItemService implements ItemServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	ItemRepository itemrepo;

	@Autowired
	UserRepository userrepo;

	@Autowired
	private ModelMapper mapper;

	// Add a new item with its supplier
	@Override
	public ItemDto addItems(ItemDto dto) {
		logger.info("Adding new item: {}", dto.getName());

		User supplier = userrepo.findById(dto.getSupplierId())
				.orElseThrow(() -> new ResourceNotFoundException("Supplier", "Id", dto.getSupplierId()));

		Item item = mapper.map(dto, Item.class);
		item.setSupplier(supplier);

		itemrepo.save(item);
		logger.info("Item added with ID: {}", item.getId());

		return mapper.map(item, ItemDto.class);
	}

	// Retrieve all items
	@Override
	public List<ItemDto> getAllItems() {
		logger.info("Fetching all items");

		List<Item> items = itemrepo.findAll();
		List<ItemDto> res = new ArrayList<>();
		for (Item it : items) {
			res.add(mapper.map(it, ItemDto.class));
		}
		logger.info("Total items found: {}", res.size());
		return res;
	}

	// Get item by ID
	@Override
	public ItemDto getById(Long id) {
		logger.info("Fetching item with ID: {}", id);

		Item item = itemrepo.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item with this id is not found"));
		return mapper.map(item, ItemDto.class);
	}

	// Update item details
	@Override
	public ItemDto updateItem(Long id, ItemDto dto) {
		logger.info("Updating item with ID: {}", id);

		Item item = itemrepo.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));

		User supplier = userrepo.findById(dto.getSupplierId())
				.orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", dto.getSupplierId()));

		item.setName(dto.getName());
		item.setCategory(dto.getCategory());
		item.setSupplier(supplier); // update supplier relation
		item.setDatetime(dto.getDatetime());

		Item updated = itemrepo.save(item);
		logger.info("Item updated with ID: {}", id);
		return mapper.map(updated, ItemDto.class);
	}

	// Delete item by ID
	@Override
	public void deleteItem(Long id) {
		logger.info("Deleting item with ID: {}", id);

		Item item = itemrepo.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("The item with this id is not found"));
		itemrepo.delete(item);
		logger.info("Item deleted with ID: {}", id);
	}
}
