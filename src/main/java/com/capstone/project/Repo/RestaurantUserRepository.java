package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.RestaurantUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Long> {
    RestaurantUser findByEmail(String email);
}
