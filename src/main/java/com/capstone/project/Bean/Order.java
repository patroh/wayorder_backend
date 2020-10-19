package com.capstone.project.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Restaurant restaurant;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderPlacedTime = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.MERGE)
    private TakeoutOrder isTakeOutOrder;

    @OneToOne(cascade = CascadeType.MERGE)
    private InRestaurantOrder isInRestaurantOrder;

    @OneToOne(cascade = CascadeType.MERGE)
    private Reservation isDineIn;

    @OneToOne
    private User user;

    private Float tax;

    private Float total;
}
