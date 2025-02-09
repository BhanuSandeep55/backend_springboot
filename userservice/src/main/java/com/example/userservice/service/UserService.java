package com.example.userservice.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User saveUser(User user) {
		Optional<User> retrievedUser = userRepository.findByEmail(user.getEmail());
		if (!retrievedUser.isPresent()) {
			user.setPassword(Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
			return userRepository.save(user);
		}
		return null;
	}
	
	

	public User loginUser(User user) {
		Optional<User> retrievedUser = userRepository.findByUserNameAndPassword(user.getUserName(), Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
		if (retrievedUser.isPresent()) {
			return retrievedUser.get();
		}
		return null;
	}
	
	public boolean changePassword(String userName,String schoolName,String newPassword) {
		Optional<User> retrievedUser = userRepository.findByUserNameAndSchoolName(userName,schoolName);
		if (retrievedUser.isPresent()) {
			retrievedUser.get().setPassword(Base64.getEncoder().encodeToString(newPassword.getBytes()));
			userRepository.save(retrievedUser.get());
			return true;
		}
		else {
			return false;
		}
		
	}
	
}
