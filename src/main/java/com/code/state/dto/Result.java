package com.code.state.dto;

import lombok.Data;

@Data
public class Result {

    private boolean success;

    private String message;

    public static Result from(boolean success, String message) {
        Result result = new Result();
        result.message = message;
        result.success = success;
        return result;
    }
}
