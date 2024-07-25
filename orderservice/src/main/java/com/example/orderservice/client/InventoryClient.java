package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.orderservice.model.InventoryResponse;

@FeignClient(name = "inventory-service", url = "${inventory-host-url:http://localhost:8082}")
public interface InventoryClient {
	@GetMapping("/inventory/check")
	boolean checkInventory(@RequestParam String product, @RequestParam int quantity);

	@GetMapping("/inventory/{id}")
	ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Long id);

	@PutMapping("/inventory/stock")
	ResponseEntity<Object> updateStock(@RequestParam String product, @RequestParam int quantity,
			@RequestParam boolean isStockIncrease);
}
