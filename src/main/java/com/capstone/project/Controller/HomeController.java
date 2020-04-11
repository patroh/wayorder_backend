package com.capstone.project.Controller;

import com.capstone.project.Bean.EmailNewsletter;
import com.capstone.project.Repo.EmailNewsletterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class HomeController {
    private EmailNewsletterRepository emailNewsletterRepository;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("email",new EmailNewsletter());
        return "index";
    }

    @PostMapping("/addEmail")
    public String addEmailToNewsletter(Model model){

        model.addAttribute("email",new EmailNewsletter());
        return "index";
    }
}
