package com.capstone.project.Controller;
/**
 * @author Rohan Patel
 * @author Vishvakumar Mavani
 */

import com.capstone.project.Bean.EmailNewsletter;
import com.capstone.project.Bean.Holders.ReturnData;
import com.capstone.project.Repo.EmailNewsletterRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ApiController {
    private final EmailNewsletterRepository emailNewsletterRepository;


    /**
     * <p>
     *     Method to send a text message to customer on the phone number entered.
     *     For development purpose, the phone number should be authorized on twilio to
     *     allow sending message
     * </p>
     * @author Vishvakumar Mavani
     * @param phoneNumber
     * @return
     */
    @CrossOrigin("*")
    @PostMapping("/message")
    public ReturnData sendSms(@RequestBody String phoneNumber) {
        ReturnData returnData = new ReturnData();
        returnData.setMessage("");
        final String ACCOUNT_SID =
                "AC5f03e6e5a19a1457523f7769642bfd09";
        final String AUTH_TOKEN =
                "bac11c77fd57d94b8767fa22f1e634cf";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


        Message message = Message
                .creator(new PhoneNumber("+1" + phoneNumber), // to
                        new PhoneNumber("+12515128841"), // from
                        "Thank you for your interest in WayOrder.\n" +
                                "We will send you download link when the app is ready :)")
                .create();

        returnData.setMessage("" + message.getSid());
        return returnData;
    }

    @GetMapping("/emailNewsletters")
    public List<EmailNewsletter> getAllEmailNewsletter() {
        return emailNewsletterRepository.findAll();
    }


}
