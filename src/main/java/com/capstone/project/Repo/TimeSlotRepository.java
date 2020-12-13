package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
