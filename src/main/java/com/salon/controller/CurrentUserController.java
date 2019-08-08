package com.salon.controller;

import com.salon.entity.Client;
import com.salon.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping(path="/api/me",
        produces="application/json")
@CrossOrigin(origins="*")
public class CurrentUserController {

    private final ClientService clientService;

    public CurrentUserController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public Client getCurrentUser(Principal principal){
        return clientService.findByUsername(principal.getName()).orElseThrow(NoSuchElementException::new);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
