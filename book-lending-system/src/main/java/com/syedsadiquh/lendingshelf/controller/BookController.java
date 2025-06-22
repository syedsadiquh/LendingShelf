package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.BookDto.BookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import com.syedsadiquh.lendingshelf.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping({"/v1/book/createBook", "/v1/book/createBook/"})
    public ResponseEntity<BaseResponse<Book>> createBook(@RequestBody BookDto bookDto) {
        var res = bookService.createBook(bookDto);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } else {
            if (res.getMessage().equals("Book already exists")) {
                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
