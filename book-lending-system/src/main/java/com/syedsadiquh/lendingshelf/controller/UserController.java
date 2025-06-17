package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.UserDto;
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
        else
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
