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
import java.util.UUID;

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

    @PostMapping({"/v1/borrowing/returnBorrowing", "/v1/borrowing/returnBorrowing/"})
    public ResponseEntity<BaseResponse<Borrowing>> returnBorrowing(
            @RequestParam UUID borrowing_id
    ) {
        var res = borrowingService.returnBorrowing(borrowing_id);
        if  (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("Unable"))
                return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
            if (res.getMessage().contains("not found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            if (res.getMessage().contains("already returned"))
                return new ResponseEntity<>(res, HttpStatus.CONFLICT);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/v1/borrowing/getActiveBorrowing", "/v1/borrowing/getActiveBorrowing/"})
    public ResponseEntity<BaseResponse<List<Borrowing>>> getActiveBorrowing(@RequestParam UUID user_id) {
        var res = borrowingService.getAllActiveBorrowings(user_id);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("not found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
