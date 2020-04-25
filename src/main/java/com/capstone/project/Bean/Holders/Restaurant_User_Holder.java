package com.capstone.project.Bean.Holders;

import com.capstone.project.Bean.Restaurant;
import com.capstone.project.Bean.RestaurantUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant_User_Holder {
    private Restaurant restaurant;
    private RestaurantUser restaurantUser;
}
