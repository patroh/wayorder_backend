package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.TimeSlotForDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotForDayRepository extends JpaRepository<TimeSlotForDay, Long> {
}
