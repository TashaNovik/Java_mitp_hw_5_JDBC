package com.example.hellospring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hellospring.exception.MessageNotFoundException;
import com.example.hellospring.model.Message;
import com.example.hellospring.model.User;
import com.example.hellospring.repository.JdbcMessageRepository;
import com.example.hellospring.repository.JdbcUserRepository;

@Service
public class MessageService {
    private final JdbcMessageRepository messageRepository;
    private final JdbcUserRepository userRepository;

    public MessageService(JdbcMessageRepository messageRepository, JdbcUserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Message findMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException("Сообщение с ID " + id + " не найдено"));
    }

    public List<Message> getMessages(int page, int size) {
        return messageRepository.findAllPaginated(page, size);
    }

    public Message saveMessage(Message message) {
        // Если автор не указан или не имеет username, используем Anonymous
        if (message.getAuthor() == null || message.getAuthor().getUsername() == null || 
            message.getAuthor().getUsername().trim().isEmpty()) {
            User anonymous = userRepository.findByUsername("Anonymous")
                    .orElseThrow(() -> new IllegalStateException("Пользователь 'Anonymous' не найден в БД."));
            message.setAuthor(anonymous);
        } else {
            // Если указан username, найдем или создадим пользователя
            String username = message.getAuthor().getUsername();
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setUsername(username);
                        return userRepository.save(newUser);
                    });
            message.setAuthor(user);
        }
        return messageRepository.save(message);
    }

    public void saveAll(List<Message> messages) {
        messageRepository.saveAll(messages);
    }

    public List<Message> findMessagesByAuthor(String username) {
        return messageRepository.findByAuthorUsername(username);
    }
}
