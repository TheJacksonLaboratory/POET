package org.monarchinitiative.poet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/private")
public class UserController {

    @GetMapping(value = "/", headers = "Accept=application/json")
    public ResponseEntity<?> testPrivate() {
        return ResponseEntity.ok().build();
    }
}
