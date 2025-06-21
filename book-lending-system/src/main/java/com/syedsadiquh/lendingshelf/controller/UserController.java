package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.UserDto.UpdateUserDto;
import com.syedsadiquh.lendingshelf.dto.UserDto.UpdateUserUsernameDto;
import com.syedsadiquh.lendingshelf.dto.UserDto.UserDto;
import com.syedsadiquh.lendingshelf.models.User;
import com.syedsadiquh.lendingshelf.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @PostMapping({"/v1/createUser", "/v1/createUser/"})
    public ResponseEntity<BaseResponse<UserDto>> createUser(@RequestBody @Valid UserDto userDto) {
        var res = userService.createUser(userDto);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
    }

    @GetMapping({"/v1/getAllUsers", "/v1/getAllUsers/"})
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        var res = userService.getAllUsers();
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping({"/v1/getUserById", "/v1/getUserById/"})
    public ResponseEntity<BaseResponse<User>> getUserById(@RequestParam UUID id) {
        var res = userService.getUserById(id);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().equals("No user found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/v1/getUserByUsername", "/v1/getUserByUsername/"})
    public ResponseEntity<BaseResponse<User>> getUserByUsername(@RequestParam String username) {
        var res = userService.getUserByUsername(username);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().equals("No user found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/v1/updateUser", "/v1/updateUser/"})
    public ResponseEntity<BaseResponse<User>> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        var res = userService.updateUser(updateUserDto);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().equals("No user found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/v1/updateUsername", "/v1/updateUsername/"})
    public ResponseEntity<BaseResponse<User>> updateUsername(@RequestBody @Valid UpdateUserUsernameDto updateUsernameDto) {
        var res = userService.updateUsername(updateUsernameDto.getOldUsername(), updateUsernameDto.getNewUsername());
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else{
            if (res.getMessage().contains("No user found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"/v1/deleteUser", "/v1/deleteUser/"})
    public ResponseEntity<BaseResponse<String>> deleteUser(@RequestParam String username) {
        var res = userService.deleteUser(username);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("User not found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
