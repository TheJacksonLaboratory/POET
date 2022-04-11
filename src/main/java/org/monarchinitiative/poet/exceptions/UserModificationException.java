package org.monarchinitiative.poet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class UserModificationException extends RuntimeException  {

    public UserModificationException(String orcid){
        super(String.format("Attemping to assign user %s, someone already has this orcid.", orcid));
    }
}
