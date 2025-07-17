package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.BorrowingDto.BorrowingDto;
import com.syedsadiquh.lendingshelf.dto.BorrowingDto.CreateBorrowingDto;
import com.syedsadiquh.lendingshelf.models.Borrowing;
import com.syedsadiquh.lendingshelf.service.BorrowingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BorrowingController {

    BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping({"/v1/borrowing/createBorrowing", "/v1/borrowing/createBorrowing/"})
    public ResponseEntity<BaseResponse<BorrowingDto>> createBorrowing(@RequestBody CreateBorrowingDto createBorrowingDto) {
        var res = borrowingService.createBorrowing(createBorrowingDto);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("not found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/v1/borrowing/getAllBorrowing", "/v1/borrowing/getAllBorrowing/"})
    public ResponseEntity<BaseResponse<List<Borrowing>>> getAllBorrowing(
            @RequestParam Optional<String> username,
            @RequestParam Optional<String> bookTitle
    ) {
        var res = borrowingService.getAllBorrowing(username, bookTitle);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("not found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }
}
