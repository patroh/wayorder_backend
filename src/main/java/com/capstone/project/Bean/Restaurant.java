package com.capstone.project.Bean;
/**
 * @author Rohan Patel
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String logo = "https://images.all-free-download.com/images/graphicthumb/vector_food_pattern_241247.jpg";
    private String bgImg = "https://images.unsplash.com/photo-1505826759037-406b40feb4cd?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80";
    private Long unit;
    private String street;
    private String city;
    private String postal;
    private String phone;
    private boolean isDineIn;
    private boolean isTakeout = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Dish> dishes = new ArrayList<>();

    @OneToOne
    private Menu menu;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RestaurantTable> tables = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<BusinessHours> businessHours = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<TimeSlotForDay> bookingTimeSlots = new ArrayList<>();
}
