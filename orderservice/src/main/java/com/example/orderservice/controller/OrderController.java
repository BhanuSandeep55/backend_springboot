package com.example.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderResponse;
import com.example.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<Object> placeOrder(@RequestBody Order order) {
		Order createdOrder = orderService.placeOrder(order);
		if (createdOrder == null) {
			return new ResponseEntity<>("Product is not available", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getAllOrders() {
		List<OrderResponse> allOrders = orderService.getAllOrders();
		return new ResponseEntity<>(allOrders, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
		OrderResponse orderResponse = orderService.getOrderById(id);
		if (orderResponse != null) {
			return new ResponseEntity<>(orderResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>("No order found associated with the given id", HttpStatus.NOT_FOUND);
		
	}

	@PutMapping("/cancel/{id}")
	public ResponseEntity<Object> cancelOrder(@PathVariable Long id) {
		Order cancelOrder = orderService.cancelOrder(id);
		if (cancelOrder != null) {
			return new ResponseEntity<>("The order has been canceled successfully.", HttpStatus.OK);
		}
		return new ResponseEntity<>("No order found with the specified ID", HttpStatus.NOT_FOUND);
	}

}
