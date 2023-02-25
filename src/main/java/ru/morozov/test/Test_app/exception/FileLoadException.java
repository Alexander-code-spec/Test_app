package ru.morozov.test.Test_app.exception;

public class FileLoadException extends RuntimeException {
    public FileLoadException(final String message){
        super(message);
    }
}
