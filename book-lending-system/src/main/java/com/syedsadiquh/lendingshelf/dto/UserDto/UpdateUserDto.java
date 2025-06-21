package com.syedsadiquh.lendingshelf.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserDto {
    @NotBlank
    String username;

    String name;

    String email;
}
