package com.salon.controller;

import com.salon.controller.exceptions.NotUniqueUsernameException;
import com.salon.dto.MasterDTO;
import com.salon.entity.Master;
import com.salon.entity.Service;
import com.salon.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping(path="api/masters",
        produces="application/json")
@CrossOrigin(origins="*")
public class MasterController {

    private final MasterService masterService;

    public MasterController(MasterService masterService) {
        this.masterService = masterService;
    }

    @GetMapping
    public List<Master> getAllMasters(){
        return masterService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Master createMaster(@RequestBody @Valid MasterDTO master){

        log.info("username - {}", master.getUsername());
        log.info("firstName - {}", master.getFirstName());
        log.info("lastName - {}", master.getLastName());
        log.info("email - {}", master.getEmail());
        log.info("password - {}", master.getPassword());
        log.info("instagram - {}", master.getInstagram());
        log.info("services - {}", master.getServices().get(0));
        log.info("position - {}", master.getPosition());

        return masterService.save(master);
    }

    @GetMapping("/{masterId}")
    public ResponseEntity<Master> getMaster(@PathVariable Long masterId){
        try{
            return new ResponseEntity<>( masterService.findById(masterId) , HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{masterId}/services")
    public List<Service> findMastersServices(@PathVariable Long masterId){
        return masterService.findById(masterId).getServices();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("ejfiow");
    }

}
