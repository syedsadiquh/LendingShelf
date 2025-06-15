package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.UserDto;
import com.syedsadiquh.lendingshelf.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({"/v1/createUser", "/v1/createUser/"})
    public ResponseEntity<BaseResponse<UserDto>>  createUser(@RequestBody @Valid UserDto userDto){
        var res = userService.createUser(userDto);
        if (res)
            return ResponseEntity.ok(new BaseResponse<>(true, "User Created", userDto));
        else
            return ResponseEntity.ok(new BaseResponse<>(false, "User Not Created", userDto));
    }
}
