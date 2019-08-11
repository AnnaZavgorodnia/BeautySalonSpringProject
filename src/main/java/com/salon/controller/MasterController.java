package com.salon.controller;

import com.salon.controller.error.ResponseError;
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
@RequestMapping(path="/api/masters",
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
        return masterService.save(master);
    }


    @DeleteMapping
    public void deleteAllMasters(){
        masterService.deleteAll();
    }

    @GetMapping("/{masterId}")
    public Master getMaster(@PathVariable Long masterId){
         return masterService.findById(masterId);
    }

    @GetMapping("/{masterId}/services")
    public List<Service> findMastersServices(@PathVariable Long masterId){
        return masterService.findById(masterId).getServices();
    }

    @PostMapping("/{masterId}/image")
    public Master updateMasterImagePath(@PathVariable Long masterId,
                                        @RequestParam("imagePath") String imagePath){
        return masterService.updateImage(masterId, imagePath);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotUniqueUsernameException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(NotUniqueUsernameException ex) {
        return new ResponseError(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(RuntimeException ex) {
        return new ResponseError(ex.getMessage());
    }

}
