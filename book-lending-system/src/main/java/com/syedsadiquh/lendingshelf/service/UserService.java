package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.dto.UserDto;
import com.syedsadiquh.lendingshelf.models.User;
import com.syedsadiquh.lendingshelf.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);
    UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(UserDto userDto) {
        try {
            var data = User.builder()
                    .createdAt(LocalDateTime.now())
                    .username(userDto.getUsername())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();
            userRepository.save(data);
            log.info("User created");
        } catch (Exception ex) {
            log.error("User creation failed. Exception: {}", ex.getMessage());
            return false;
        }
        return true;
    }
}
