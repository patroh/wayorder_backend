package com.capstone.project.Controller;

import com.capstone.project.Bean.Order;
import com.capstone.project.Bean.OrderItem;
import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Bean.User;
import com.capstone.project.Repo.OrderItemRepository;
import com.capstone.project.Repo.OrderRepository;
import com.capstone.project.Repo.RestaurantRepository;
import com.capstone.project.Repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class HomeController {
    private RestaurantRepository restaurantRepo;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;

    //Return the restaurant object
    @GetMapping("/restaurant")
    public Restaurant getRestaurant(@RequestParam Long id)
    {
        Restaurant foundRestaurant=restaurantRepo.findById(id).get();
        return foundRestaurant;
    }


    //Makes an order from the items , save to repository and return the saved order
    @PostMapping(value = "/order/{id}/{uid}", produces = "application/json")
    public Order makeOrder(@RequestBody List<OrderItem> items,@PathVariable Long id,@PathVariable Long uid){
        float total = 0;
        User user = userRepository.findById(uid).get();
        List<OrderItem> savedItems = orderItemRepository.saveAll(items);
        for(OrderItem o: savedItems){
            total += (o.getQuantity() * o.getDish().getPrice());
        }
        Restaurant restaurant = restaurantRepo.findById(id).get();
        Order newOrder = Order.builder().restaurant(restaurant).orderItems(items).total(total).build();

        Order placedOrder = orderRepository.save(newOrder);
        user.getOrders().add(placedOrder);
        userRepository.save(user);
        return placedOrder;
    }


    // Register user in database by checking if it already exist or not
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


    //Authenticate user with email and password provided
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


    //Returns all orders  -- for testing purpose, remove in production
    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    //Returns all users  -- for testing purpose, remove in production
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }




}
