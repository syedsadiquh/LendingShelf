package com.syedsadiquh.lendingshelf.dto.BorrowingDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateBorrowingDto {

    @NotBlank
    private UUID user_id;

    @NotBlank
    private UUID book_id;

    private LocalDateTime expectedReturnDate;
}
