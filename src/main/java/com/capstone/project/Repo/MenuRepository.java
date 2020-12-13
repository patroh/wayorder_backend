package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
