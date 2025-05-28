package com.supplytracker.service;

import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	ItemRepository itemrepo;
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public ItemDto addItems(ItemDto dto) {
		User supplier = userrepo.findById(dto.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier", "Id", dto.getSupplierId()));
		Item item = mapper.map(dto, Item.class);
		itemrepo.save(item);
		
		return mapper.map(item, ItemDto.class);
	}



	@Override
	public List<ItemDto> getAllItems() {
		List<Item> items = itemrepo.findAll();
		List<ItemDto> res = new ArrayList<>();
		for (Item it : items) {
			res.add(mapper.map(it, ItemDto.class));
		}

		return res;
	}

	@Override
	public ItemDto getById(Long id){
		Item item = itemrepo.findById(id).orElseThrow(()-> new ItemNotFoundException("Item with this id is not found"));
		return mapper.map(item,  ItemDto.class);
	}
	
	
	
	@Override
	public ItemDto updateItem(Long id, ItemDto dto) {
		Item item = itemrepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item not found" + "id: " + id));
		
		User supplier = userrepo.findById(dto.getSupplierId()).orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", dto.getSupplierId()));
		item.setName(dto.getName());
		item.setCategory(dto.getCategory());
//		item.setSupplierId(dto.getSupplierId());
		item.setDatetime(dto.getDatetime());
		Item updated = itemrepo.save(item);
		return mapper.map(updated, ItemDto.class);
	}
	
	@Override
	public void deleteItem(Long id) {
	    Item item = itemrepo.findById(id)
	        .orElseThrow(() -> new ItemNotFoundException("The item with this id is not found"));
	    itemrepo.delete(item);
	}


}

