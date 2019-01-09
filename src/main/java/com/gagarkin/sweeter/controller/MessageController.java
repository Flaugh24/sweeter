package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.Message;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.domain.dto.MessageDto;
import com.gagarkin.sweeter.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
    public String messages(@RequestParam(required = false) String filter,
                           Model model,
                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                           @AuthenticationPrincipal User user) {

        Page<MessageDto> page;
        if (filter == null || filter.isBlank())
            page = messageService.findAll(user, pageable);
        else
            page = messageService.findByTag(filter,user, pageable);

        model.addAttribute("page", page);
        model.addAttribute("url", "/messages");
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping
    public String add(@AuthenticationPrincipal User user,
                      @Valid Message message,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam("file") MultipartFile file,
                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {

        if (bindingResult.hasErrors()) {

            log.error("has errors");

            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("message", message);
            model.addAttribute("page", messageService.findAll(user, pageable));
            model.addAttribute("url", "/messages");

            return "main";
        } else {
            messageService.addMessage(message, user, file);
        }

        return "redirect:/messages";
    }

    @GetMapping("/user/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<MessageDto> page = messageService.findByUser(user, currentUser, pageable);

        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("url", user.getId());

        return "userMessages";
    }

    @PostMapping("/user/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file) throws IOException {

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

    @GetMapping("/{message}/like")
    public String likeMessage(@AuthenticationPrincipal User currentUser,
                              @PathVariable Message message,
                              RedirectAttributes redirectAttributes,
                              @RequestHeader(required = false) String referer){
        Set<User> likes = message.getLikes();
        if(likes.contains(currentUser)){
            likes.remove(currentUser);
        }else {
            likes.add(currentUser);
        }

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(referer).build();
        uriComponents.getQueryParams()
                .forEach(redirectAttributes::addAttribute);

        return "redirect:" + uriComponents.getPath();
    }
}
