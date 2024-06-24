package com.ra.project_module4.exception;


public class UserBlockException extends RuntimeException {

    private String username;

    public UserBlockException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


}
