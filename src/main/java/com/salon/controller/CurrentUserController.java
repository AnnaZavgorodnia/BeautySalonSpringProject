package com.salon.controller;

import com.salon.controller.error.ResponseError;
import com.salon.entity.Client;
import com.salon.entity.User;
import com.salon.service.ClientService;
import com.salon.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping(path="/api/me",
        produces="application/json")
@CrossOrigin(origins="*")
public class CurrentUserController {

    private final UserService userService;

    public CurrentUserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','MASTER')")
    @GetMapping
    public User getCurrentUser(Principal principal){
        return userService.findByUsername(principal.getName()).orElseThrow(NoSuchElementException::new);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseError handleRuntimeException(RuntimeException ex) {
        return new ResponseError(ex.getMessage());
    }
}
