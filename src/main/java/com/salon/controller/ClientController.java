package com.salon.controller;

import com.salon.controller.exceptions.NotUniqueUsernameException;
import com.salon.dto.ClientDTO;
import com.salon.entity.Client;
import com.salon.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping(path="/api/clients",
        produces="application/json")
@CrossOrigin(origins="*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients(){
        return clientService.getAll();
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody @Valid ClientDTO client){
        try {
            return new ResponseEntity<>(clientService.save(client),HttpStatus.CREATED);
        } catch (NotUniqueUsernameException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getMaster(@PathVariable Long clientId){
        try{
            return new ResponseEntity<>( clientService.findById(clientId) , HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
