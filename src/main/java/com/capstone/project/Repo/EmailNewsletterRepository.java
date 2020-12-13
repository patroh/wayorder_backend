package com.capstone.project.Repo;
/**
 * @author Rohan Patel
 */
import com.capstone.project.Bean.EmailNewsletter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNewsletterRepository extends JpaRepository<EmailNewsletter, Long> {
}
