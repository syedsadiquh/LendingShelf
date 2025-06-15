package com.syedsadiquh.lendingshelf.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank
    String username;

    @NotBlank
    String name;

    String email;

}
