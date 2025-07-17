package com.syedsadiquh.lendingshelf.dto.BorrowingDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingDto {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID bookId;
    private String bookTitle;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private LocalDateTime createdAt;
}