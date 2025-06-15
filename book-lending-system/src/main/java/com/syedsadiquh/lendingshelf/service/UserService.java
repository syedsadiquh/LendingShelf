package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.controller.BaseResponse;
import com.syedsadiquh.lendingshelf.dto.UserDto;
import com.syedsadiquh.lendingshelf.models.User;
import com.syedsadiquh.lendingshelf.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    public BaseResponse<UserDto> createUser(UserDto userDto) {
        try {
            var data = User.builder()
                    .createdAt(LocalDateTime.now())
                    .username(userDto.getUsername())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();
            userRepository.save(data);
            log.info("User created");
            return new BaseResponse<>(true, "User Created successfully", userDto);
        } catch (DataIntegrityViolationException ex) {
            log.error("Exception: User creation failed: user already exists.");
            return new BaseResponse<>(false, "User Already Exists", userDto);
        } catch (Exception ex) {
            log.error("User creation failed unexpectedly. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unexpected Error. Please Try again", userDto);
        }
    }
}
