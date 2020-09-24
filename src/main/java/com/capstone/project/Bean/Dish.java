package com.capstone.project.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Lob
    private String description;
    private Float price;
    private Float tax;
    private Float star;
    private Integer reviews;
    private Integer likes;
    private String image = "https://png.pngtree.com/png-vector/20190318/ourlarge/pngtree-junk-food-seamless-pattern-doodle-drawing-style-line-art-hand-png-image_858333.jpg";
}
