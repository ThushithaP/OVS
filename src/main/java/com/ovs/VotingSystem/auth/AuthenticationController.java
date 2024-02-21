package com.ovs.VotingSystem.auth;

import com.ovs.VotingSystem.config.JwtService;
import com.ovs.VotingSystem.user.User;
import com.ovs.VotingSystem.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository repository;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String register(RegisterRequest request, Model model) {
        AuthenticationResponse response = service.register(request);
        model.addAttribute("message",response.getMessage());
        return "auth/register";
    }

    @GetMapping("/login")
    public String login(Model model ,HttpServletResponse response) {
        model.addAttribute("loginRequest", new AuthenticateRequest());
        Cookie cookie = new Cookie("Authorize",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "auth/login";
    }
    @PostMapping("/authenticate")
    public String authenticate( AuthenticateRequest request, HttpServletResponse res, Model model) {
        try{
            AuthenticationResponse response = service.authenticate(request, res);
            return "redirect:/position/index";

        } catch (Exception e){
            model.addAttribute("message","Login Failed !.");
            return "auth/login";
        }
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }
}


