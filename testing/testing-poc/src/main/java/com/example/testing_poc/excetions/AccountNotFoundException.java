package com.example.testing_poc.excetions;

import com.example.testing_poc.config.ApiException;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends ApiException {

    public AccountNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "ACCOUNT_NOT_FOUND",
                "Account with id " + id + " not found",
                new Object[]{id}
        );
    }
}