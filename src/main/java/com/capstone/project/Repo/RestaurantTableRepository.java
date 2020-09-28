package com.capstone.project.Repo;

import com.capstone.project.Bean.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    RestaurantTable findByNumber(Long number);
}
