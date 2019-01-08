package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.Message;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
@Controller
@RequestMapping("messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public String messages(@RequestParam(required = false) String filter, Model model) {

        List<Message> messages;
        if (filter == null || filter.isBlank())
            messages = messageService.findAll();
        else
            messages = messageService.findByTag(filter);

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping
    public String add(@AuthenticationPrincipal User user,
                      @Valid Message message,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {

            log.error("has errors");

            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("message", message);
            model.addAttribute("messages", messageService.findAll());

            return "main";
        } else {
            messageService.addMessage(message, user, file);
        }

        model.addAttribute("messages", messageService.findAll());

        return "redirect:/messages";
    }

    @GetMapping("/user/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        Set<Message> messages = user.getMessages();

        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }

    @PostMapping("/user/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (message.getAuthor() != null && message.getAuthor().equals(currentUser)) {
            if (!text.isBlank()) {
                message.setText(text);
            }

            if (!tag.isBlank()) {
                message.setTag(tag);
            }

            messageService.updateMessage(message, file);
        }

        return "redirect:/messages/user/" + user;
    }
}
