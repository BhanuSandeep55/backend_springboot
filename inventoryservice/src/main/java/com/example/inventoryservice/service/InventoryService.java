package com.example.inventoryservice.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;

	public boolean isProductAvailable(String product, int quantity) {
		return inventoryRepository.findByProduct(product).map(inventory -> inventory.getStock() >= quantity)
				.orElse(false);
	}

	public Inventory saveInventory(String product, int stock, MultipartFile image, Double price, Integer rating) throws IOException {
		Inventory inventory = new Inventory();
		inventory.setProduct(product);
		inventory.setStock(stock);
		inventory.setPrice(price);
		inventory.setRating(rating);
		inventory.setMimeType(image.getContentType());
		// inventory.setImage("data:" + image.getContentType() + ";base64," +imageEncodeToString);
		inventory.setImage(image.getBytes());
		Optional<Inventory> byProduct = inventoryRepository.findByProduct(inventory.getProduct());
		if (!byProduct.isPresent()) {
			return inventoryRepository.save(inventory);
		}
		return null;
	}

	public Inventory updateInventory(String product, int stock) {
		Inventory inventory = null;
		Optional<Inventory> retrievedInventory = inventoryRepository.findByProduct(product);
		if (retrievedInventory.isPresent()) {
			inventory = retrievedInventory.get();
			inventory.setStock(stock);
			inventoryRepository.save(inventory);
		}
		return inventory;
	}

	public List<Inventory> getAllInventory() {
		return inventoryRepository.findAll();

	}

	public InventoryResponse getInventoryById(Long id) {
		InventoryResponse inventoryResponse = new InventoryResponse();
		Optional<Inventory> retrievdInventory = inventoryRepository.findById(id);
		if (retrievdInventory.isPresent()) {
			Inventory inventoryBody = retrievdInventory.get();
			inventoryResponse.setId(inventoryBody.getId());
			inventoryResponse.setProduct(inventoryBody.getProduct());
			inventoryResponse.setStock(inventoryBody.getStock());
			inventoryResponse.setImage(inventoryBody.getImage());
			inventoryResponse.setMimeType(inventoryBody.getMimeType());
			return inventoryResponse;
		}
		return null;
	}

	public Inventory updateStock(String product, int quantity, boolean isStockIncrease) {
		Inventory inventory = null;
		Optional<Inventory> retrievedInventory = inventoryRepository.findByProduct(product);
		if (retrievedInventory.isPresent()) {
			inventory = retrievedInventory.get();
			inventory.setStock(isStockIncrease ? inventory.getStock() + quantity : inventory.getStock() - quantity);
			inventoryRepository.save(inventory);
		}
		return inventory;
	}

	public Inventory deleteInventoryById(Long id) {
		Optional<Inventory> retrievedInventory = inventoryRepository.findById(id);
		if (retrievedInventory.isPresent()) {
			inventoryRepository.deleteById(id);
			return retrievedInventory.get();
		}
		return null;
	}
}
