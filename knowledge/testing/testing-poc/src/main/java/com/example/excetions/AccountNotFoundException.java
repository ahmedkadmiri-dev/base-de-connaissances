package com.example.excetions;

import org.springframework.http.HttpStatus;
import java.util.Map;
import com.example.exception.ApiException;

public class AccountNotFoundException extends ApiException {

    public AccountNotFoundException(Long id) {
        super(
                HttpStatus.NOT_FOUND,
                "ACCOUNT_NOT_FOUND",
                new Object[]{id},
                Map.of("accountId", id)
        );
    }
}