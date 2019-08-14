package com.salon.controller;

import com.salon.controller.error.ResponseError;
import com.salon.dto.ServiceDTO;
import com.salon.entity.Service;
import com.salon.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path="/api/services",
        produces="application/json")
@CrossOrigin(origins="*")
@PreAuthorize("hasAuthority('ADMIN')")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<Service> getAll(){
        return serviceService.findAll();
    }

    @DeleteMapping
    public void deleteAllServices(){
        serviceService.deleteAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Service createService(@RequestBody ServiceDTO service){
        return serviceService.create(service);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseError handleRuntimeException(RuntimeException ex) {
        return new ResponseError(ex.getMessage());
    }

}
