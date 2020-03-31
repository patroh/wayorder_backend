package com.capstone.project.Repo;

import com.capstone.project.Bean.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish,Long> {
}
