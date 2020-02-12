package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.Role;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.domain.dto.ProfileDto;
import com.gagarkin.sweeter.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String users(@AuthenticationPrincipal User currentUser,
                        Model model){

        List<User> users = userService.findAll();
        users.remove(currentUser);

        model.addAttribute("users", users);

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String user(@PathVariable User user, Model model){

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String save(@RequestParam("userId") User user,
                       @RequestParam Map<String, String> form){

        userService.saveUser(user, form);


        return "redirect:/users";
    }

    @GetMapping("profile")
    public String profile(@AuthenticationPrincipal User user,
                          Model model){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @Valid ProfileDto profileDto,
                                BindingResult bindingResult,
                                Model model){

        log.info(profileDto);

        boolean passwordsAreDifferent = !profileDto.getPasswordNew().isEmpty() && !profileDto.getPasswordNew().equals(profileDto.getPasswordConfirm());
        if(passwordsAreDifferent) {
            model.addAttribute("passwordNewError", "Passwords are different!");
            model.addAttribute("passwordConfirmError", "Passwords are different!");
        }

        boolean wrongPassword = userService.updateProfile(user, profileDto);
        if(!wrongPassword){
            model.addAttribute("passwordError", "Wrong password!");
        }

        if(bindingResult.hasErrors() || passwordsAreDifferent || !wrongPassword){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", profileDto.getEmail());
            return "profile";
        }


        return "redirect:/users/profile";

    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        userService.subscribe(currentUser, user);

        return "redirect:/messages/user/" + user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        userService.unsubscribe(currentUser, user);

        return "redirect:/messages/user/" + user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable User user,
            @PathVariable String type
    ) {
        model.addAttribute("userChannel", user);
        model.addAttribute("type", type);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        } else {
            model.addAttribute("users", user.getSubscribers());
        }

        return "subscriptions";
    }
}

