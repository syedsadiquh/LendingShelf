package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.controller.BaseResponse;
import com.syedsadiquh.lendingshelf.dto.UserDto.UserDto;
import com.syedsadiquh.lendingshelf.models.Borrowing;
import com.syedsadiquh.lendingshelf.models.User;
import com.syedsadiquh.lendingshelf.repositories.UserRepository;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
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
            user = userRepository.findUsersById(id);
            if (user == null) {
                log.info("No user found");
                return new BaseResponse<>(false, "No user found with Id : " + id, null);
            }
            log.info("User Found with Id : {}", id);
            return new BaseResponse<>(true, "User found", user);
        } catch (Exception ex) {
            log.error("Unable to get User by Id. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unable to get User by Id", null);
        }
    }

    public BaseResponse<User> getUserByUsername(String username) {
        User user;
        try {
            user = userRepository.findUsersByUsername(username);
            if (user == null) {
                log.info("No user found");
                return new BaseResponse<>(false, "No user found with username : " + username, null);
            }
            log.info("User Found with Username : {}", username);
            return new BaseResponse<>(true, "User found", user);
        } catch (Exception ex) {
            log.error("Unable to get User by Username. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unable to get User by username", null);
        }
    }

    public BaseResponse<User> updateUser(com.syedsadiquh.lendingshelf.dto.UserDto.@Valid UpdateUserDto updateUserDto) {
        User oldUser;
        try {
            oldUser = userRepository.findUsersByUsername(updateUserDto.getUsername());
            if (oldUser == null) {
                log.warn("No User Found with Username : {}", updateUserDto.getUsername());
                return new BaseResponse<>(false, "No User Found with username: " + updateUserDto.getUsername(), null);
            }
            var anyUser = userRepository.findUsersByEmail(updateUserDto.getEmail());
            if(anyUser != null) {
                log.warn("Email Already Exists");
                return new BaseResponse<>(false, "Email Already Exists", null);
            }

            var newName = Objects.equals(updateUserDto.getName(), "") ? oldUser.getName() : updateUserDto.getName();
            var newEmail = Objects.equals(updateUserDto.getEmail(), "") ? oldUser.getEmail() : updateUserDto.getEmail();

            var res = userRepository.updateUser(updateUserDto.getUsername(), LocalDateTime.now(), updateUserDto.getUsername(), newName, newEmail);

            if (res == 1) {
                var data = userRepository.findUsersByUsername(updateUserDto.getUsername());
                log.info("User Updated Successfully");
                return new BaseResponse<>(true, "User Updated Successfully", data);
            } else {
                log.error("Unable to Update User");
                return new BaseResponse<>(false, "Internal Server Error", oldUser);
            }
        } catch (Exception ex) {
            log.error("User Not Found. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unable to get User with username: " + updateUserDto.getUsername(), null);
        }
    }

    public BaseResponse<User> updateUsername(String oldUsername, String newUsername) {
        try {
            var oldUser = userRepository.findUsersByUsername(oldUsername);
            if (oldUser == null) {
                log.warn("No user found with Username : {}", oldUsername);
                return new BaseResponse<>(false, "No user found with Username : " + oldUsername, oldUser);
            }
            var res = userRepository.updateUsername(oldUser.getId(), newUsername);
            if (res == 1) {
                var data = userRepository.findUsersByUsername(newUsername);
                log.info("User Updated Successfully");
                return new BaseResponse<>(true, "User Updated Successfully", data);
            } else {
                log.error("Unable to Update User");
                return new BaseResponse<>(false, "Internal Server Error", oldUser);
            }
        } catch (Exception ex) {
            log.error("Unable to update Username. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Unable to get User with username: " + oldUsername, null);
        }
    }

    public BaseResponse<String> deleteUser(String username) {
        try {
            var user = userRepository.findUsersByUsername(username);
            if (user == null) {
                log.warn("No user found with Username : {}", username);
                return new BaseResponse<>(false, "User not found with username : " + username, null);
            }
            var borrows = user.getBorrowings();
            boolean isBorrowed = false;
            for (Borrowing x : borrows) {
                if (x.getActualReturnDate() != null) {
                    isBorrowed = true;
                    break;
                }
            }
            if (isBorrowed) {
                log.warn("Borrowings found for Username : {}", username);
                return new BaseResponse<>(false, "Borrowings found for username : " + username, null);
            }
            var res = userRepository.deleteUserByUsername(username);
            if (res == 1) {
                return new BaseResponse<>(true, "User Deleted Successfully", null);
            } else {
                log.error("Unable to delete User");
                return new BaseResponse<>(false, "Internal Server Error. Try again later", null);
            }
        } catch (Exception ex) {
            log.error("Unable to delete User. Exception: {}", ex.getMessage());
            return new BaseResponse<>(false, "Internal Server Error. Try again later", null);
        }
    }
}
