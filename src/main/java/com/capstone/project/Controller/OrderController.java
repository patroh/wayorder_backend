package com.capstone.project.Controller;

import com.capstone.project.Bean.Order;
import com.capstone.project.Bean.OrderItem;
import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Bean.User;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private RestaurantRepository restaurantRepo;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;


    //Makes an order from the items , save to repository and return the saved order
    @PostMapping(value = "/{id}/{uid}", produces = "application/json")
    public Order makeOrder(@RequestBody List<OrderItem> items, @PathVariable Long id, @PathVariable Long uid){
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



    // Get all orders placed by a particular user
    @PostMapping("/{uid}")
    public List<Order> findOrdersByUser(@PathVariable Long uid){
        return  userRepository.findById(uid).get().getOrders();
    }


    //Returns all orders  -- for testing purpose, remove in production
    @GetMapping("/orders")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }


}
