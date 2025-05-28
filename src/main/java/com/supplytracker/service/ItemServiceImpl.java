package com.supplytracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.entity.Item;
import com.supplytracker.entity.User;
import com.supplytracker.exception.ResourceNotFoundException;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.UserRepositry;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	ItemRepository itemrepo;
	
	@Autowired
	UserRepositry userrepo;
	
	@Override
	public ItemDto addItems(ItemDto dto) {
		User supplier = UserRepositry.findById(dto.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier", "Id", dto.getSupplierId()));
		Item item = new Item();
		item.setName(dto.getName());
		item.setCategory(dto.getCategory());
		item.setSupplier(supplier);
		item.setDatetime(dto.getDatetime());
		
		Item saved = itemrepo.save(item);
		dto.setId(saved.getId());
		return dto;
	}
	
	@Override
	public List<ItemDto> getAllItems(){
		return itemrepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}
	
	@Override
	public ItemDto getById(Long id) {
		Item item = itemrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		return mapToDto(item);
	}
	
	@Override
	public ItemDto updateItem(Long id, ItemDto dto) {
		Item item = itemrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		
		User supplier = userrepo.findById(dto.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", dto.getSupplierId()));
		item.setName(dto.getName());
		item.setCategory(dto.getCategory());
		item.setSupplier(dto.getSupplierId());
		item.setDatetime(dto.getDatetime());
		Item updated = itemrepo.save(item);
		return mapToDto(updated);
	}
	
	@Override
	public void deleteItem(Long id) {
		Item item = itemrepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item", "id", id));
		itemrepo.delete(item);
	}
	private ItemDto mapToDto(Item item) {
		return new ItemDto(
			item.getId(),
			item.getName(),
			item.getCategory(),
			item.getSupplier().getId();
			item.getDatetime()
		);
	}
}
