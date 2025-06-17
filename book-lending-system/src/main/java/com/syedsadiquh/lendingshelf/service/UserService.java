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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            return new BaseResponse<>(true, "User Created Successfully", userDto);
        } catch (DataIntegrityViolationException ex) {
            log.error("Exception: User creation failed: user already exists.");
            return new BaseResponse<>(false, "User Already Exists", userDto);
        } catch (Exception ex) {
            log.error("User creation failed unexpectedly. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unexpected Error. Please Try Again", userDto);
        }
    }

    public BaseResponse<List<User>> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try {
            allUsers = userRepository.findAll();
            if (allUsers.isEmpty()) {
                log.info("No users found");
                return new BaseResponse<>(true, "No users found", allUsers);
            }
            log.info("All Users Found");
            return new BaseResponse<>(true, "All Users Found", allUsers);
        } catch (Exception ex) {
            log.error("Unable to get all users. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unable to get all users", allUsers);
        }
    }

    public BaseResponse<User> getUserById(UUID id) {
        User user;
        try {
            user = userRepository.findById(id).orElse(null);
            if (user == null) {
                log.info("No user found");
                return new BaseResponse<>(true, "No user found with Id : "+id, null);
            }
            log.info("User Found with Id : {}", id);
            return new BaseResponse<>(true, "User found", user);
        }  catch (Exception ex) {
            log.error("Unable to get User. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unable to get User by Id", null);
        }
    }
}
