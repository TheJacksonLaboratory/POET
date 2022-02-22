package org.monarchinitiative.poet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String username) {
        super(String.format("User %s is not authorized to perform this action.", username));
    }

    public AuthenticationException(boolean malformed) {
        super(String.format("Authentication or authentication claims are malformed."));
    }
}
