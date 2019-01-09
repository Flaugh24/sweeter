package com.gagarkin.sweeter.domain.dto;

import com.gagarkin.sweeter.domain.Message;
import com.gagarkin.sweeter.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MessageDto {

    private final Long id;
    private final String text;
    private final String tag;
    private final User author;
    private final String fileName;
    private final Long likes;
    private final Boolean meLiked;

    public MessageDto(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.tag = message.getTag();
        this.author = message.getAuthor();
        this.fileName = message.getFileName();
        this.likes = likes;
        this.meLiked = meLiked;
    }
}
