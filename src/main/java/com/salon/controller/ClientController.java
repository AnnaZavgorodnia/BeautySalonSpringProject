package com.salon.controller;

import com.salon.controller.error.ResponseError;
import com.salon.controller.exceptions.NotUniqueUsernameException;
import com.salon.dto.ClientDTO;
import com.salon.entity.Client;
import com.salon.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

    private final MessageSource messageSource;
    private final ClientService clientService;

    public ClientController(ClientService clientService,
                            MessageSource messageSource) {
        this.messageSource = messageSource;
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getAllClients(){
        return clientService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Client createClient(@RequestBody @Valid ClientDTO client){
        return clientService.save(client);
    }


    @DeleteMapping
    public void deleteAllClients(){
        clientService.deleteAll();
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/{clientId}")
    public Client getMaster(@PathVariable Long clientId){
        return clientService.findById(clientId);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseError conflict() {
        return new ResponseError("Username already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public ResponseError notFound(NoSuchElementException e) {
        return new ResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(RuntimeException ex) {
        return new ResponseError(ex.getMessage());
    }

}
