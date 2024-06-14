package com.ra.project_module4.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataException extends Exception{
    private String fieldError;
    public DataException(String fieldError, String message) {
        super(message);
    }
}