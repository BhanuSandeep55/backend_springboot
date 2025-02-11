package com.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Object> saveUser(@RequestBody User user) {
		User savedUser = userService.saveUser(user);
		if (savedUser != null) {
			return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("This email is already in use. Please use a different email address.",
				HttpStatus.CONFLICT);
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody User user) {
		User loginUser = userService.loginUser(user);
		if (!(loginUser == null)) {
			return new ResponseEntity<>(loginUser, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}
	
	
	@GetMapping("/changePassword")
	public ResponseEntity<Object> login(@RequestParam("userName") String userName,@RequestParam("schoolName") String schoolName, @RequestParam("newPassword") String newPassword) {
		boolean result = userService.changePassword(userName, schoolName, newPassword);
		if (result) {
			return new ResponseEntity<>("password changed successfully", HttpStatus.OK);
		}
		return new ResponseEntity<>("Invalid credentials",HttpStatus.UNAUTHORIZED);
	}
}
