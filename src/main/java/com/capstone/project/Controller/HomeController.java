package com.capstone.project.Controller;

import com.capstone.project.Bean.Order;
import com.capstone.project.Bean.OrderItem;
import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Repo.OrderItemRepository;
import com.capstone.project.Repo.OrderRepository;
import com.capstone.project.Repo.RestaurantRepository;
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

    @GetMapping("/restaurant")
    public Restaurant getRestaurant(@RequestParam Long id)
    {
        Restaurant foundRestaurant=restaurantRepo.findById(id).get();
        return foundRestaurant;
    }

    @PutMapping("/order")
    public boolean makeOrder(@RequestBody List<OrderItem> items,@RequestBody Long restaurantID){
        boolean status = false;
        List<OrderItem> savedItems = orderItemRepository.saveAll(items);
        Restaurant restaurant = restaurantRepo.findById(restaurantID).get();
        Order newOrder = Order.builder().restaurant(restaurant).orderItems(savedItems).build();
        orderRepository.save(newOrder);
        return status;
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }


}
