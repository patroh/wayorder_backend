package com.capstone.project.Controller;

import com.capstone.project.Bean.*;
import com.capstone.project.Repo.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {
    private EmailNewsletterRepository emailNewsletterRepository;


    @GetMapping("/emailNewsletters")
    public List<EmailNewsletter> getAllEmailNewsletter(){return emailNewsletterRepository.findAll();}




}
