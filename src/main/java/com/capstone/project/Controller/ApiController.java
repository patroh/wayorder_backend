package com.capstone.project.Controller;

import com.capstone.project.Bean.EmailNewsletter;
import com.capstone.project.Repo.EmailNewsletterRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {
    private final EmailNewsletterRepository emailNewsletterRepository;


    @GetMapping("/emailNewsletters")
    public List<EmailNewsletter> getAllEmailNewsletter() {
        return emailNewsletterRepository.findAll();
    }


}
