package com.syedsadiquh.lendingshelf.service;

import com.syedsadiquh.lendingshelf.controller.BaseResponse;
import com.syedsadiquh.lendingshelf.dto.BookDto.BookDto;
import com.syedsadiquh.lendingshelf.dto.BookDto.SearchBookDto;
import com.syedsadiquh.lendingshelf.dto.BookDto.UpdateBookDto;
import com.syedsadiquh.lendingshelf.models.Book;
import com.syedsadiquh.lendingshelf.repositories.BookRepository;
import com.syedsadiquh.lendingshelf.specifications.BookSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
            var res = bookRepository.getBookByIsbn(isbn);
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

    public BaseResponse<Book> updateBook(UUID id, UpdateBookDto updateBookDto) {
        try {
            var oldBook = bookRepository.getBookById(id);
            if (oldBook == null) {
                log.info("Book not found");
                return new BaseResponse<>(false, "Book not found", null);
            }
            var newTitle = (updateBookDto.getTitle() != null && !updateBookDto.getTitle().isBlank()) ? updateBookDto.getTitle() : oldBook.getTitle();
            var newAuthor = (updateBookDto.getAuthor() != null && !updateBookDto.getAuthor().isBlank()) ? updateBookDto.getAuthor() : oldBook.getAuthor();
            var newIsbn = (updateBookDto.getIsbn() != null && !updateBookDto.getIsbn().isBlank()) ? updateBookDto.getIsbn() : oldBook.getIsbn();
            var newPubYear = updateBookDto.getPublicationYear() != null ? (updateBookDto.getPublicationYear() == oldBook.getPublicationYear() ? oldBook.getPublicationYear() : updateBookDto.getPublicationYear()) : oldBook.getPublicationYear();
            var newQuantity = updateBookDto.getAvailableQuantity() != null ? (updateBookDto.getAvailableQuantity() == oldBook.getAvailableQuantity() ? oldBook.getAvailableQuantity() : updateBookDto.getAvailableQuantity()) : oldBook.getAvailableQuantity();

            var res = bookRepository.updateBookByIsbn(oldBook.getId(), LocalDateTime.now(),newTitle, newAuthor, newIsbn , newPubYear, newQuantity);
            if (res == 1) {
                log.info("Book updated");
                var data = bookRepository.getBookById(id);
                return new BaseResponse<>(true, "Book updated", data);
            } else {
                log.error("Book not updated");
                return new BaseResponse<>(false, "Book not updated", null);
            }
        } catch (Exception e) {
            log.error("Unable to update book. Exception: {}", e.getMessage());
            return new BaseResponse<>(false, "Unable to Update Book", null);
        }
    }

    public BaseResponse<Page<Book>> searchBook(SearchBookDto searchBookDto, Pageable pageable) {
        try {
            var spec = BookSpecification.matchFields(searchBookDto);
            var res = bookRepository.findAll(spec, pageable);
            if (res.isEmpty()) {
                log.info("No Books found");
                return new BaseResponse<>(false, "No Books found", null);
            }
            log.info("Books found");
            return new BaseResponse<>(true, "Books found", res);
        } catch (Exception e) {
            log.error("Unable to search book. Exception: {}", e.getMessage());
            return new BaseResponse<>(false, "Unable to Search Book", null);
        }
    }

}
