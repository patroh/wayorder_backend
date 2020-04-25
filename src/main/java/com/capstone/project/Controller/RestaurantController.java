package com.capstone.project.Controller;

import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
public class RestaurantController {

    private RestaurantRepository restaurantRepo;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;
    private EmailNewsletterRepository emailNewsletterRepository;
    private RestaurantUserRepository restaurantUserRepository;


    //Return the restaurant object
    @GetMapping()
    public Restaurant getRestaurant(@RequestParam Long id)
    {
        Restaurant foundRestaurant=restaurantRepo.findById(id).get();
        return foundRestaurant;
    }


}
