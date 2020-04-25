package com.capstone.project.Controller;

import com.capstone.project.Bean.User;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private RestaurantRepository restaurantRepo;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;
    private EmailNewsletterRepository emailNewsletterRepository;
    private RestaurantUserRepository restaurantUserRepository;


    // Register customer user in database by checking if it already exist or not
    @PutMapping(value = "/register" , consumes = "application/json")
    public User registerUser(@RequestBody User user)
    {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if(foundUser == null){
            foundUser = userRepository.save(user);
            return foundUser;
        }
        return null;
    }

    //Authenticate customer user with email and password provided
    @PostMapping(value = "/login", consumes = "application/json")
    public User loginUser(@RequestBody User user)
    {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if(foundUser == null){
            return null;
        }
        if(foundUser.getPassword().equals(user.getPassword()))
            return foundUser;
        return null;
    }

    //Returns all users  -- for testing purpose, remove in production
    @GetMapping("/displayAll")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
