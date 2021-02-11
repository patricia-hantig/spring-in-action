package com.patricia.tacocloud.security;

import com.patricia.tacocloud.data.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // here we inject UserRepository & PasswordEncoder in RegistrationController so that they can now be used in the next methods

    @GetMapping
    public String registerForm() {
        return "registration";
    }
    // - a GET request will be handled by this method which returns a logical view name of registration -> registration.html

    @PostMapping
    public String processRegistration(RegistrationForm registrationForm) {
        userRepository.save(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
    // when the form is submitted the HTTP POST request will be handled by this method
    // the RegistrationForm object given to this method is bound to the request data & is defined within the class RegistrationForm.java
    // toUser() method from RegistrationForm uses RegistrationForm properties to create a new User object
    // this new User object -> will be saved in processRegistration() method using injected UserRepository
    // RegistrationController is injected with a PasswordEncoder - which is the same PasswordEncoder declared in SecurityConfig.java
    // when processing a form submission => RegistrationController passes it to the toUser() method - which uses it to encode the password before saving it to the database
    //  -> in this way the submitted password is encoded & the user details service will be able to authenticate against that encoded password

}
// @Controller - to show it is a controller & to mark it for component scanning
// @RequestMapping("/register") - will handle requests for whose path is '/register'

