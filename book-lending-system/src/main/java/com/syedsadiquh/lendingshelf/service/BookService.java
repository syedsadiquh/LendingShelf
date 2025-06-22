package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.controller.BaseResponse;
import com.syedsadiquh.lendingshelf.dto.BookDto.BookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import com.syedsadiquh.lendingshelf.repositories.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookService {
    private static final Logger log = LogManager.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BaseResponse<Book> createBook(BookDto bookDto) {
        try {
            var data = Book.builder()
                    .createdAt(LocalDateTime.now())
                    .title(bookDto.getTitle())
                    .author(bookDto.getAuthor())
                    .isbn(bookDto.getIsbn().replace("-", ""))
                    .publicationYear(bookDto.getPublicationYear())
                    .availableQuantity(bookDto.getAvailableQuantity())
                    .build();
            bookRepository.save(data);
            log.info("Book created");
            return new BaseResponse<>(true, "New Book Created", data);
        } catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                log.info("Book already exists");
                return new BaseResponse<>(false, "Book already exists", null);
            }
            log.error("Unable to create book");
            return new BaseResponse<>(false, "Unable to Create New Book", null);
        }
    }

    
}
