package com.example.inventoryservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventoryController {
	@Autowired
	private InventoryService inventoryService;

	@GetMapping("/check")
	public boolean checkInventory(@RequestParam String product, @RequestParam int quantity) {
		return inventoryService.isProductAvailable(product, quantity);
	}

	@PostMapping
	public ResponseEntity<Object> addInventory(@RequestParam String product, @RequestParam int stock,
			@RequestParam("price") Double price,
			@RequestParam("rating") Integer rating,
			@RequestParam("image") MultipartFile image) throws IOException {
		Inventory savedInventory = inventoryService.saveInventory(product, stock, image,price,rating);
		if (savedInventory != null) {
			return new ResponseEntity<>("The Product is added successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Product is already available in the Inventory", HttpStatus.NOT_ACCEPTABLE);
	}

	@GetMapping
	public ResponseEntity<List<Inventory>> getAllInventory() {
		List<Inventory> allInventory = inventoryService.getAllInventory();
		return new ResponseEntity<>(allInventory, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getInventoryById(@PathVariable Long id) {
		InventoryResponse inventoryResponse = inventoryService.getInventoryById(id);
		if (inventoryResponse == null) {
			return new ResponseEntity<>("No inventory found associated with the given id", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
	}

	@PutMapping("/stock")
	public ResponseEntity<Object> updateStock(@RequestParam String product, @RequestParam int quantity,
			@RequestParam boolean isStockIncrease) {
		Inventory updatedStock = inventoryService.updateStock(product, quantity, isStockIncrease);
		if (updatedStock == null) {
			return new ResponseEntity<>("Product is not available, please add it to the inventory",
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updatedStock, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Object> updateInventory(@RequestParam String product, @RequestParam int stock) {
		Inventory updatedInventory = inventoryService.updateInventory(product, stock);
		if (updatedInventory == null) {
			return new ResponseEntity<>("Product is not available, Please add it to the inventory",
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(updatedInventory, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteInventoryById(@PathVariable Long id) {
		Inventory inventory = inventoryService.deleteInventoryById(id);
		if (inventory == null) {
			return new ResponseEntity<>("No inventory is found associated with the given ID", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
	}
}
