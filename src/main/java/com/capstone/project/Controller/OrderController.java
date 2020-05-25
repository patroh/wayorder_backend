package com.capstone.project.Controller;

import com.capstone.project.Bean.Holders.ReturnData;
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
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private RestaurantRepository restaurantRepo;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;


    //Makes an order from the items , save to repository and return the saved order
    @PostMapping(value = "/{id}/{uid}", produces = "application/json")
    public Order makeOrder(@RequestBody List<OrderItem> items, @PathVariable Long id, @PathVariable Long uid) {
        float total = 0;
        User user = userRepository.findById(uid).get();
        List<OrderItem> savedItems = orderItemRepository.saveAll(items);
        for (OrderItem o : savedItems) {
            total += (o.getQuantity() * o.getDish().getPrice());
        }
        Restaurant restaurant = restaurantRepo.findById(id).get();
        Order newOrder = Order.builder().restaurant(restaurant).user(user).orderItems(items).total(total).build();

        Order placedOrder = orderRepository.save(newOrder);
        return placedOrder;
    }


    //Get all orders of the restaurant
    @CrossOrigin("*")
    @GetMapping("/restaurant/{id}")
    public ReturnData getAllOrderOfRestaurant(@PathVariable("id") Long id){
        ReturnData returnData = new ReturnData();
        List<Order> allOrders = orderRepository.findByRestaurant_Id(id);
        returnData.setCode(0);
        returnData.setMessage("All orders retrived");
        returnData.setObject(allOrders);
        return returnData;
    }

    // Get all orders placed by a particular user
    @PostMapping("/{uid}")
    public List<Order> findOrdersByUser(@PathVariable Long uid) {
        return orderRepository.findByUser_Id(uid);
    }


    //Returns all orders  -- for testing purpose, remove in production
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


}
