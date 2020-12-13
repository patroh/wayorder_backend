package com.capstone.project.Bean;
/**
 * @author Rohan Patel
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime time;

    @OneToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SeatingArrangement> seatingArrangement;


}
