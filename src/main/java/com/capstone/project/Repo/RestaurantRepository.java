package com.capstone.project.Repo;

import com.capstone.project.Bean.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    Restaurant findByNameAndUnitAndStreetAndCityAndPostalAndPhone(String name,Long unit,String street,String city,String postal,String phone);

}
