package com.capstone.project.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TableReservation {
    @Id
    private Long id;
    @OneToOne
    private User user;
    @OneToOne
    private RestaurantTable table;
    private LocalDateTime start;
    private LocalDateTime end;

}
