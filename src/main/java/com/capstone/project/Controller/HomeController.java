package com.capstone.project.Controller;

import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Repo.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HomeController {
    private RestaurantRepository restaurantRepo;

    @GetMapping("/api/restaurant")
    public Restaurant getRestaurant(@RequestParam Long id)
    {
        Restaurant foundRestaurant=restaurantRepo.findById(id).get();
        return foundRestaurant;
    }


}
