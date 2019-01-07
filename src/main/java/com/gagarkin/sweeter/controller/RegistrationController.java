package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String addUser(User user, Map<String, Object> model) {
        if (userService.addUser(user))
            return "redirect:/login";
        else
            model.put("message", "User already exists!");
            return "registration";

    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){

        boolean isActivated = userService.activateUser(code);
        if(isActivated)
            model.addAttribute("message", "User successfully activated");
        else
            model.addAttribute("message", "Activation code is not found!");

        return "login";
    }
}
