package com.capstone.project.Repo;

import com.capstone.project.Bean.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
