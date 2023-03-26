package com.exam.helper;

public class UserFoundException extends Exception{

    public UserFoundException(String msg) {
        super(msg);
    }

    public UserFoundException() {
        super("The user with this username is already there in DB. Change the username.");
    }
}
