package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.BookDto.BookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import com.syedsadiquh.lendingshelf.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping({"/v1/book/findBook", "/v1/book/findBook/"})
    public ResponseEntity<BaseResponse<Book>> findBook(@RequestParam String isbn) {
        var res = bookService.findBookByIsbn(isbn);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().equals("Book not found")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
