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

    //Return the restaurant object
    @GetMapping("/restaurant")
    public Restaurant getRestaurant(@RequestParam Long id)
    {
        Restaurant foundRestaurant=restaurantRepo.findById(id).get();
        return foundRestaurant;
    }


    //Makes an order from the items , save to repository and return the saved order
    @PostMapping(value = "/order/{id}", produces = "application/json")
    public Order makeOrder(@RequestBody List<OrderItem> items,@PathVariable Long id){
        float total = 0;
        List<OrderItem> savedItems = orderItemRepository.saveAll(items);
        for(OrderItem o: savedItems){
            total += (o.getQuantity() * o.getDish().getPrice());
        }
        Restaurant restaurant = restaurantRepo.findById(id).get();
        Order newOrder = Order.builder().restaurant(restaurant).orderItems(items).total(total).build();

        Order placedOrder = orderRepository.save(newOrder);
        return placedOrder;
    }


    //Returns all orders
    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }


}
