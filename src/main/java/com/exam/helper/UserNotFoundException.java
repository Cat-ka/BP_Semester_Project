package com.exam.helper;

import com.exam.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Member;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException() {
        super("User with this username not found in DB.");
    }
}


