package com.syedsadiquh.lendingshelf.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
