package com.capstone.project.Repo;

import com.capstone.project.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long>{
	public User findByEmail(String email);
}
