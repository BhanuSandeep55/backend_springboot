package com.example.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUserName(String userName);

	Optional<User> findByUserNameAndPassword(String userName, String password);
	
	Optional<User> findByUserNameAndSchoolName(String userName, String schoolName);

}
