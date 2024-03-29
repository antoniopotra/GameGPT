package software.design.gamegpt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import software.design.gamegpt.model.User;
import software.design.gamegpt.service.UserService;
import software.design.gamegpt.utils.Validator;

@Controller
public class UserController {
    private final UserService userService;
    private final Validator emailValidator = new Validator("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private final Validator passwordValidator = new Validator("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,}$");

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (!emailValidator.validate(user.getEmail())) {
            result.rejectValue("email", "invalid_email_format", "Invalid email format.");
        }
        if (!passwordValidator.validate(user.getPassword())) {
            result.rejectValue("password", "invalid_password_format", "Password must contain at least 8 characters and one of each: uppercase, lowercase, digit, symbol.");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            result.rejectValue("username", "username_exists", "Username already in use.");
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            result.rejectValue("email", "email_exists", "Email already in use.");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.save(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}