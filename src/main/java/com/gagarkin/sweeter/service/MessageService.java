package com.gagarkin.sweeter.service;

import com.gagarkin.sweeter.domain.Message;
import com.gagarkin.sweeter.domain.User;
import com.gagarkin.sweeter.domain.dto.MessageDto;
import com.gagarkin.sweeter.repository.MessageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addMessage(Message message, User author, MultipartFile file) throws IOException {
        message.setAuthor(author);
        saveFile(message, file);
        messageRepository.save(message);
    }

    private void saveFile(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists())
                uploadDir.mkdir();

            log.info(uploadPath);

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            message.setFileName(resultFilename);

            file.transferTo(new File(uploadPath + "/" + resultFilename));
        }
    }

    public void updateMessage(Message message, MultipartFile file) throws IOException {
        saveFile(message, file);
        messageRepository.save(message);
    }

    public Page<MessageDto> findAll(User user, Pageable pageable) {
        return messageRepository.findAll(user, pageable);
    }

    public Page<MessageDto> findByTag(String filter, User user, Pageable pageable) {
       return messageRepository.findByTag(filter, user, pageable);
    }

    public Page<MessageDto> findByUser(User author, User user, Pageable pageable){
        return messageRepository.findByUser(author, user, pageable);
    }

    public void deleteById(Long id){
        messageRepository.deleteById(id);
    }
}
