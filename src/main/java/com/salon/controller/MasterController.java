package com.salon.controller;

import com.salon.controller.error.ResponseError;
import com.salon.dto.MasterDTO;
import com.salon.entity.Master;
import com.salon.entity.Service;
import com.salon.service.MasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    @GetMapping
    public List<Master> getAllMasters(){
        return masterService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Master createMaster(@RequestBody @Valid MasterDTO master){
        return masterService.save(master);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public void deleteAllMasters(){
        masterService.deleteAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MASTER','CLIENT')")
    @GetMapping("/{masterId}")
    public Master getMaster(@PathVariable Long masterId){
         return masterService.findById(masterId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    @GetMapping("/{masterId}/services")
    public List<Service> findMastersServices(@PathVariable Long masterId){
        return masterService.findById(masterId).getServices();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{masterId}/image")
    public Master updateMasterImagePath(@PathVariable Long masterId,
                                        @RequestParam("imagePath") String imagePath){
        return masterService.updateImage(masterId, imagePath);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseError conflict() {
        return new ResponseError("Appointment already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(RuntimeException ex) {
        return new ResponseError(ex.getMessage());
    }

}
