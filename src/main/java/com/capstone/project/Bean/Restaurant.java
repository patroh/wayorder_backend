package com.capstone.project.Bean;

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
    private Long unit;
    private String street;
    private String city;
    private String postal;
    private String phone;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Dish> dishes = new ArrayList<>();

    @OneToOne
    private Menu menu;
}
