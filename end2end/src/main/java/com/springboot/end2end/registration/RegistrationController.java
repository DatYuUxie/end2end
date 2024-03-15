package com.springboot.end2end.registration;


import com.springboot.end2end.event.RegistrationCompleteEvent;
import com.springboot.end2end.registration.token.VerificationToken;
import com.springboot.end2end.registration.token.VerificationTokenRepository;
import com.springboot.end2end.registration.token.VerificationTokenService;
import com.springboot.end2end.user.IUserService;
import com.springboot.end2end.user.User;
import com.springboot.end2end.utility.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;


    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new RegistrationRequest());
        return "registration";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registration, HttpServletRequest request){
        User user = userService.registerUser(registration);
        //public the verification email event here
        publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
        return"redirect:/registration//registration-form?success";
    }


    @GetMapping("/verifyEmail") // trùng với listener
    public String verifyEmail(@RequestParam("token") String token){
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if(theToken.isPresent() && theToken.get().getUser().isEnabled()){
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }


}
