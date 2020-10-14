package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Bean.Holders.OrderItems_OrderType_Holder;
import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Repo.*;
import com.pusher.rest.Pusher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final RestaurantRepository restaurantRepo;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final TakeOutOrderRepository takeOutOrderRepository;


    //Makes an order from the items , save to repository and return the saved order.
    @PostMapping(value = "/{id}/{uid}", produces = "application/json")
    public Order makeOrder(@RequestBody OrderItems_OrderType_Holder holder, @PathVariable Long id, @PathVariable Long uid) {
        Pusher pusher = new Pusher("1009241", "a15e9f068cfceb6d6e26", "b1529b200107a0afcbcd");
        float total = 0;
        float tax = 0;
        User user = userRepository.findById(uid).get();
        List<OrderItem> savedItems = orderItemRepository.saveAll(holder.getItems());
        for (OrderItem o : savedItems) {
            float quantity = o.getQuantity();
            float dishPrice = o.getDish().getPrice();
            float dishTax = o.getDish().getTax();
            total += (quantity * dishPrice);
            tax += (quantity * ((dishPrice * dishTax) / 100));
        }
        Restaurant restaurant = restaurantRepo.findById(id).get();

        Order newOrder = Order.builder().restaurant(restaurant).user(user).
                orderItems(holder.getItems()).total(total).tax(tax).build();
        if(holder.getDineInOrder()!=null){

        }else if (holder.getTakeoutOrder()!=null){
            TakeoutOrder newTakeOutOrder = takeOutOrderRepository.save(holder.getTakeoutOrder());
            newOrder.setIsTakeOutOrder(newTakeOutOrder);
        }else if (holder.getInRestaurantOrder()!=null){

        }
        Order placedOrder = orderRepository.save(newOrder);

        pusher.setCluster("us2");
        pusher.setEncrypted(true);

        pusher.trigger("restaurant-channel" + id, "orderPlaced", placedOrder);
        return placedOrder;
    }

    //Get all orders of the restaurant
    @CrossOrigin("*")
    @GetMapping("/restaurant/{id}")
    public ReturnData getAllOrderOfRestaurant(@PathVariable("id") Long id) {
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
