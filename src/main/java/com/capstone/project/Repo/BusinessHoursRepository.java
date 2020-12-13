package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {
}
