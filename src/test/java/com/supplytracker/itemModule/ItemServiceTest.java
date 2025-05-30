package com.supplytracker.itemModule;

import com.supplytracker.dto.ItemDto;
import com.supplytracker.entity.Item;
import com.supplytracker.entity.User;
import com.supplytracker.exception.ItemNotFoundException;
import com.supplytracker.exception.ResourceNotFoundException;
import com.supplytracker.repository.ItemRepository;
import com.supplytracker.repository.UserRepository;
import com.supplytracker.service.Imp.ItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    private Item item;
    private ItemDto itemDto;
    private User supplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        supplier = new User();
        supplier.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setName("Laptop");
        item.setCategory("Electronics");
        item.setDatetime(LocalDateTime.now());
        item.setSupplier(supplier);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Laptop");
        itemDto.setCategory("Electronics");
        itemDto.setDatetime(LocalDateTime.now());
        itemDto.setSupplierId(1L);
    }

    @Test
    void addItems_ShouldReturnSavedItemDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(modelMapper.map(itemDto, Item.class)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);

        ItemDto result = itemService.addItems(itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void getAllItems_ShouldReturnListOfItemDtos() {
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);

        List<ItemDto> result = itemService.getAllItems();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnItemDto() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);

        ItemDto result = itemService.getById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void getById_WhenNotFound_ShouldThrowException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getById(1L));
    }

    @Test
    void updateItem_WhenExists_ShouldReturnUpdatedDto() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(modelMapper.map(item, ItemDto.class)).thenReturn(itemDto);

        ItemDto result = itemService.updateItem(1L, itemDto);

        assertNotNull(result);
        verify(itemRepository).save(item);
    }

    @Test
    void updateItem_WhenItemNotFound_ShouldThrowException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(1L, itemDto));
    }

    @Test
    void updateItem_WhenSupplierNotFound_ShouldThrowException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> itemService.updateItem(1L, itemDto));
    }

    @Test
    void deleteItem_WhenExists_ShouldDeleteItem() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.deleteItem(1L);

        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void deleteItem_WhenNotFound_ShouldThrowException() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.deleteItem(1L));
    }
}
