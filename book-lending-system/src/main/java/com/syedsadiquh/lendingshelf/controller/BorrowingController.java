package com.syedsadiquh.lendingshelf.controller;

import com.syedsadiquh.lendingshelf.dto.BorrowingDto.BorrowingDto;
import com.syedsadiquh.lendingshelf.dto.BorrowingDto.CreateBorrowingDto;
import com.syedsadiquh.lendingshelf.service.BorrowingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BorrowingController {

    BorrowingService borrowingService;
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping({"/v1/borrowing/createBorrowing", "/v1/borrowing/createBorrowing/"})
    public ResponseEntity<BaseResponse<BorrowingDto>> createBorrowing(@RequestBody CreateBorrowingDto createBorrowingDto){
        var res = borrowingService.createBorrowing(createBorrowingDto);
        if (res.isSuccess())
            return new ResponseEntity<>(res, HttpStatus.OK);
        else {
            if (res.getMessage().contains("not found"))
                return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }
}
