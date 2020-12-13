package com.capstone.project.Bean;
/**
 * @author Rohan Patel
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class SeatingArrangement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private RestaurantTable table;

    private Integer partySize;

    @OneToOne
    private TimeSlot timeSlot;
}
