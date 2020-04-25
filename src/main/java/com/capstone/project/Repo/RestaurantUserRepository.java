package com.capstone.project.Repo;

import com.capstone.project.Bean.RestaurantUser;
import com.capstone.project.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantUserRepository extends JpaRepository<RestaurantUser,Long> {
    RestaurantUser findByEmail(String email);
}
