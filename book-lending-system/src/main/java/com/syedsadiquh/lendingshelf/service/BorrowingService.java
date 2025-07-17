package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.controller.BaseResponse;
import com.syedsadiquh.lendingshelf.dto.BorrowingDto.BorrowingDto;
import com.syedsadiquh.lendingshelf.dto.BorrowingDto.CreateBorrowingDto;
import com.syedsadiquh.lendingshelf.models.Borrowing;
import com.syedsadiquh.lendingshelf.repositories.BookRepository;
import com.syedsadiquh.lendingshelf.repositories.BorrowingRepository;
import com.syedsadiquh.lendingshelf.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BorrowingService {

    private static final Logger log = LogManager.getLogger(BorrowingService.class);
    final BorrowingRepository borrowingRepository;
    final BookRepository bookRepository;
    final UserRepository userRepository;

    public BorrowingService(BorrowingRepository borrowingRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.borrowingRepository = borrowingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BaseResponse<BorrowingDto> createBorrowing(CreateBorrowingDto createBorrowingDto){
        try {
            var book = bookRepository.getBookById(createBorrowingDto.getBook_id());
            if(book == null){
                log.error("Book with id {} not found", createBorrowingDto.getBook_id());
                return new BaseResponse<>(false, "Book not found", null);
            }

            var user = userRepository.findById(createBorrowingDto.getUser_id());
            if(user.isEmpty()) {
                log.error("User with id {} not found", createBorrowingDto.getUser_id());
                return new BaseResponse<>(false, "User not found", null);
            }

            var borrowing = Borrowing.builder()
                    .createdAt(LocalDateTime.now())
                    .user(user.get())
                    .book(book)
                    .expectedReturnDate(createBorrowingDto.getExpectedReturnDate() == null ? LocalDateTime.now().plusDays(14) : createBorrowingDto.getExpectedReturnDate())
                    .build();

            borrowingRepository.save(borrowing);
            bookRepository.updateBookQuantityById(book.getId(), book.getAvailableQuantity()-1);
            log.info("Borrowing created successfully");

            // Convert to DTO to avoid lazy loading issues
            BorrowingDto borrowingDto = BorrowingDto.builder()
                    .id(borrowing.getId())
                    .userId(user.get().getId())
                    .userName(user.get().getUsername())
                    .bookId(book.getId())
                    .bookTitle(book.getTitle())
                    .expectedReturnDate(borrowing.getExpectedReturnDate())
                    .actualReturnDate(borrowing.getActualReturnDate())
                    .createdAt(borrowing.getCreatedAt())
                    .build();

            return new BaseResponse<>(true, "Borrowing Created", borrowingDto);
        }
        catch (Exception e){
            log.error("Unable to create borrowing - {}", e.getMessage());
            return new BaseResponse<>(false, "Borrowing not created", null);
        }

    }

}
