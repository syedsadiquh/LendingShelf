package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.BookDto.BookDto;
import com.syedsadiquh.lendingshelf.dto.BookDto.SearchBookDto;
import com.syedsadiquh.lendingshelf.dto.BookDto.UpdateBookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import com.syedsadiquh.lendingshelf.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
            if (res.getMessage().contains("Book already exists")) {
                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/v1/book/findBookByIsbn", "/v1/book/findBookByIsbn/"})
    public ResponseEntity<BaseResponse<Book>> findBookByIsbn(@RequestParam String isbn) {
        var res = bookService.findBookByIsbn(isbn);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().contains("Book not found")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/v1/book/getAllBooks", "/v1/book/getAllBooks/"})
    public ResponseEntity<BaseResponse<Page<Book>>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        var sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        var res = bookService.findAllBooks(pageable);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().contains("No Books Exists")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping({"/v1/book/updateBook", "/v1/book/updateBook/"})
    public ResponseEntity<BaseResponse<Book>> updateBook(@RequestParam UUID id, @RequestBody UpdateBookDto updateBookDto) {
        var res = bookService.updateBook(id, updateBookDto);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().contains("Book not found")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/v1/book/searchBook", "/v1/book/searchBook/"})
    public ResponseEntity<BaseResponse<Page<Book>>> searchBook(
            @RequestBody SearchBookDto searchBookDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        var sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);
        var res = bookService.searchBook(searchBookDto, pageable);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().contains("No Books found")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping({"/v1/book/deleteBook", "/v1/book/deleteBook/"})
    public ResponseEntity<BaseResponse<String>> deleteBook(@RequestParam UUID id) {
        var res = bookService.deleteBook(id);
        if (res.isSuccess()) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            if (res.getMessage().contains("Book not found")) {
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
