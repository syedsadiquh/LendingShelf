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


    @PostMapping({"/v1/user/createUser", "/v1/user/createUser/"})
    public ResponseEntity<BaseResponse<UserDto>> createUser(@RequestBody @Valid UserDto userDto) {
        var res = userService.createUser(userDto);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        else
            return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }

    @GetMapping({"/v1/user/getAllUsers", "/v1/user/getAllUsers/"})
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        var res = userService.getAllUsers();
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping({"/v1/user/findUserById", "/v1/user/findUserById/"})
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

    @GetMapping({"/v1/user/findUserByUsername", "/v1/user/findUserByUsername/"})
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

    @PostMapping({"/v1/user/updateUser", "/v1/user/updateUser/"})
    public ResponseEntity<BaseResponse<User>> updateUser(@RequestBody @Valid UpdateUserDto updateUserDto) {
        var res = userService.updateUser(updateUserDto);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("No User Found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            if (res.getMessage().contains("Email Already Exists"))
                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/v1/user/updateUsername", "/v1/user/updateUsername/"})
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

    @DeleteMapping({"/v1/user/deleteUser", "/v1/user/deleteUser/"})
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
