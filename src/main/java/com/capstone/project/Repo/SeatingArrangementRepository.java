package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.SeatingArrangement;
import com.capstone.project.Bean.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatingArrangementRepository extends JpaRepository<SeatingArrangement, Long> {
    List<SeatingArrangement> findByTimeSlot(TimeSlot timeSlot);
}
