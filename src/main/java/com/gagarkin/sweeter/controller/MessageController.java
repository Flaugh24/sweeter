package com.gagarkin.sweeter.controller;

import com.gagarkin.sweeter.domain.Message;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.repository.MessageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("messages")
public class MessageController {
    private final MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    public String messages(@RequestParam(required = false) String filter, Model model) {

        List<Message> messages;
        if (filter == null || filter.isBlank())
            messages = messageRepository.findAll();
        else
            messages = messageRepository.findByTag(filter);

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String text,
                      @RequestParam String tag,
                      @RequestParam("file") MultipartFile file,
                      Map<String, Object> model) throws IOException {

        Message message = new Message(user, text, tag);
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdir();

            log.info(uploadPath);

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            message.setFile(resultFilename);

            file.transferTo(new File(uploadPath + "/" + resultFilename));
        }


        log.info(message);

        messageRepository.save(message);
        List<Message> all = messageRepository.findAll();

        model.put("messages", all);

        return "redirect:messages";
    }
}
