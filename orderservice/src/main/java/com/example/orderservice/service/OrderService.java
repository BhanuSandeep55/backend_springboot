package com.example.orderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.model.InventoryResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderResponse;
import com.example.orderservice.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private InventoryClient inventoryClient;
	
	public Order placeOrder(Order order) {
		boolean isAvailable = inventoryClient.checkInventory(order.getProduct(), order.getQuantity());
		if (isAvailable) {
			order.setStatus("Open");
			Order savedOrder = orderRepository.save(order);
			inventoryClient.updateStock(savedOrder.getProduct(), savedOrder.getQuantity(), false);
			return savedOrder;
		} else {
			return null;
		}
	}

	public Order cancelOrder(Long id) {
		Optional<Order> Oretrievedrder = orderRepository.findById(id);
		if (Oretrievedrder.isPresent()) { 
			Order order = Oretrievedrder.get();
			order.setStatus("Cancelled");
			orderRepository.save(order);
			return order;
		}
		return null;
	}

	public List<OrderResponse> getAllOrders() {
		List<OrderResponse> orderResponseList = new ArrayList<OrderResponse>();
		List<Order> orderList = orderRepository.findAll();
		for(int i=0; i<orderList.size(); i++) {
			OrderResponse orderResponse = new OrderResponse();
			Order retrievedOrder = orderList.get(i);
			ResponseEntity<InventoryResponse> inventory = inventoryClient.getInventoryById(retrievedOrder.getProductId());
			orderResponse.setId(retrievedOrder.getId());
			orderResponse.setProductId(inventory.getBody().getId());
			orderResponse.setQuantity(orderList.get(i).getQuantity());
			orderResponse.setStatus(retrievedOrder.getStatus());
			orderResponse.setProduct(inventory.getBody().getProduct());
			orderResponse.setMimeType(inventory.getBody().getMimeType());
			orderResponse.setImage(inventory.getBody().getImage());
			orderResponse.setUserId(retrievedOrder.getUserId());
			
			orderResponseList.add(orderResponse);
		}
		return orderResponseList;
	}

	public OrderResponse getOrderById(Long id) {
		Optional<Order> retrieveOrder = orderRepository.findById(id);
		if (retrieveOrder.isPresent()) {
			ResponseEntity<InventoryResponse> inventory = inventoryClient.getInventoryById(retrieveOrder.get().getProductId());
			OrderResponse orderResponse = new OrderResponse();
			orderResponse.setId(id);
			orderResponse.setProductId(inventory.getBody().getId());
			orderResponse.setQuantity(inventory.getBody().getStock());
			orderResponse.setStatus(retrieveOrder.get().getStatus());
			orderResponse.setProduct(inventory.getBody().getProduct());
			orderResponse.setMimeType(inventory.getBody().getMimeType());
			orderResponse.setImage(inventory.getBody().getImage());
			return orderResponse;
		}
		return null;
	}
}
