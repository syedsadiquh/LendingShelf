package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.controller.BaseResponse;
import com.syedsadiquh.lendingshelf.dto.BookDto.BookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import com.syedsadiquh.lendingshelf.repositories.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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

    public BaseResponse<Book> findBookByIsbn(String isbn) {
        try {
            var res = bookRepository.findBookByIsbn(isbn);
            if (res == null) {
                log.info("Book not found");
                return new BaseResponse<>(false, "Book not found", null);
            }
            log.info("Book found");
            return new BaseResponse<>(true, "Book found", res);
        } catch (Exception e) {
            log.error("Unable to find book. Exception: {}", e.getMessage());
            return new BaseResponse<>(false, "Unable to Find Book", null);
        }
    }

    public BaseResponse<Page<Book>> findAllBooks(Pageable pageable) {
        try {
            var res = bookRepository.findAll(pageable);
            if (res.isEmpty()) {
                log.info("No Books Exists");
                return new BaseResponse<>(false, "No Books Exists in the page", res);
            }
            log.info("Books found");
            return new BaseResponse<>(true, "Books found", res);
        } catch (Exception e) {
            log.error("Unable to get all book. Exception: {}", e.getMessage());
            return new BaseResponse<>(false, "Unable to Find Book", null);
        }
    }


}
