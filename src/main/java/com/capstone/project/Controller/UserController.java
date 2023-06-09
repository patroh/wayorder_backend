package com.capstone.project.Controller;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.RestaurantUser;
import com.capstone.project.Bean.User;
import com.capstone.project.Repo.RestaurantUserRepository;
import com.capstone.project.Repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RestaurantUserRepository restaurantUserRepository;

    // Register customer user in database by checking if it already exist or not
    @PutMapping(value = "/register", consumes = "application/json")
    public User registerUser(@RequestBody User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser == null) {
            foundUser = userRepository.save(user);
            return foundUser;
        }
        return null;
    }

    //Authenticate customer user with email and password provided
    @PostMapping(value = "/login", consumes = "application/json")
    public User loginUser(@RequestBody User user) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser == null) {
            return null;
        }
        if (foundUser.getPassword().equals(user.getPassword()))
            return foundUser;
        return null;
    }

    //Login the restaurant user
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/restaurant/login", consumes = "application/json")
    public RestaurantUser loginRestaurantUsr(@RequestBody RestaurantUser restaurantUser) {
        RestaurantUser foundUser = restaurantUserRepository.findByEmail(restaurantUser.getEmail());
        if (foundUser == null) {
            return null;
        }
        if (foundUser.getPassword().equals(restaurantUser.getPassword()))
            return foundUser;
        return null;
    }


    //Returns all users  -- for testing purpose, remove in production
    @GetMapping("/displayAll")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
