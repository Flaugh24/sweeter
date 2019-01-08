package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        if(bindingResult.hasErrors()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("user", user);
            return "registration";
        }

        if(!user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordConfirmError", "Passwords are different");
            if(passwordConfirm.isBlank()){
                model.addAttribute("passwordConfirmError", "Password confirmation cannot be empty");
            }
            model.addAttribute("user", user);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User already exists!");
            return "registration";

        } else
            return "redirect:/login";

    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {

        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}

