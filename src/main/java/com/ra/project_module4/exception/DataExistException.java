package com.ra.project_module4.exception;

public class DataExistException extends Exception{
    private String field;
    public DataExistException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
