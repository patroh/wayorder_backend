package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
