package com.gagarkin.sweeter.repository;

import com.gagarkin.sweeter.domain.Message;
import com.gagarkin.sweeter.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByTag(String tag, Pageable pageable);

    Page<Message> findAll(Pageable pageable);

    Page<Message> findByAuthor(User author, Pageable pageable);
}
