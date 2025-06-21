package com.syedsadiquh.lendingshelf.dto.UserDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserUsernameDto {
    @NotBlank
    String oldUsername;

    @NotBlank
    String newUsername;

}
