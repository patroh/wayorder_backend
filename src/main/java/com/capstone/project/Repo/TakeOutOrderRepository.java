package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.TakeoutOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeOutOrderRepository extends JpaRepository<TakeoutOrder, Long> {
}
