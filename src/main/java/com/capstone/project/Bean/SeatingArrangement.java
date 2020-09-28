package com.capstone.project.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class SeatingArrangement {
    @Id
    private Long id;

    @OneToOne
    private RestaurantTable table;

    private Integer partySize;

    @OneToOne
    private TimeSlot timeSlot;
}
