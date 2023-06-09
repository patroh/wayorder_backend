package com.capstone.project.Bean.Holders;

import com.capstone.project.Bean.OrderItem;
import com.capstone.project.Bean.Reservation;
import com.capstone.project.Bean.RestaurantTable;
import com.capstone.project.Bean.TakeoutOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItems_OrderType_Holder {
    private List<OrderItem> items = new ArrayList<>();
    private TakeoutOrder takeoutOrder;
    private Reservation dineInOrder;
    private RestaurantTable inRestaurantOrder;
}
