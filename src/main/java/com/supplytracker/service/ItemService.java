package com.supplytracker.service;

import java.util.List;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.entity.Item;

public interface ItemService {
	ItemDto addItems(ItemDto itemDto);
	ItemDto updateItem(Long id, ItemDto itemDto);
	List<ItemDto> getAllItems();
	ItemDto getById(Long id);
	void deleteItem(Long id);
}
