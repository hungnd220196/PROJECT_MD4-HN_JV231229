package com.ra.project_module4.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataError<T> {
    private String message;
    private T content;
    private HttpStatus httpStatus;
}
