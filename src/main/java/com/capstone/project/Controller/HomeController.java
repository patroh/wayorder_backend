package com.capstone.project.Controller;

import com.capstone.project.Bean.Restaurant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public Restaurant getRestaurant(){
        return Restaurant.builder().id(Long.valueOf(1)).name("Dal Roti").build();
    }
}
