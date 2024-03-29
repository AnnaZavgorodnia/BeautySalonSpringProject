package com.salon.controller;

import com.salon.controller.error.ResponseError;
import com.salon.dto.UserAppointmentDTO;
import com.salon.entity.Appointment;
import com.salon.dto.AppointmentDTO;
import com.salon.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping(path="/api",
        produces="application/json")
@CrossOrigin(origins="*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    public AppointmentController(AppointmentService appointmentService,
                                 ModelMapper modelMapper) {
        this.appointmentService = appointmentService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MASTER')")
    @GetMapping("/appointments")
    @ResponseBody
    public List<UserAppointmentDTO> getAllAppointments(){

        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();

        return new ModelMapper().map(appointmentService.findAll(), listType);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/appointments")
    public Appointment createAppointment(@RequestBody @Valid AppointmentDTO appointment,
                                         Principal principal){
        log.info("IN  createAppointment ----- appointmentDTO: {} ", appointment);
        return appointmentService.save(appointment, principal.getName());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    @GetMapping("appointments/{appointmentId}")
    public Appointment getAppointmentById(@PathVariable Long appointmentId){
        log.info("IN getAppointmentById ----- appointmentId: {}", appointmentId);
        return appointmentService.findById(appointmentId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','MASTER')")
    @GetMapping("masters/{masterId}/appointments")
    public List<UserAppointmentDTO> getAllMastersAppointments(@PathVariable Long masterId){

        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();

        return new ModelMapper().map(appointmentService.findAllMastersAppointments(masterId),
                                    listType);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','MASTER')")
    @GetMapping("masters/{masterId}/appointments/{date}")
    public List<AppointmentDTO> getMastersAppointmentsByDate(
            @PathVariable Long masterId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
        log.info("IN getMastersAppointmentsByDate ------ date: {}", date);
        log.info("IN getMastersAppointmentsByDate ------ masterId: {}", masterId);
        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();
        return new ModelMapper().map(
                appointmentService.findAllMastersAppointmentsByDate(masterId, date),
                listType);
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("appointments/{appointmentId}")
    public void deleteAppointment(@PathVariable Long appointmentId) {
        log.info("IN deleteAppointment ------ appointmentId: {}", appointmentId);
        appointmentService.deleteById(appointmentId);

    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/me/appointments")
    public Page<AppointmentDTO> getCurrentUserAppointments(
            @RequestParam(value = "page") Integer pageNum,
            @RequestParam(value = "size") Integer size,
            Principal principal){

        log.info("IN getCurrentUserAppointments ------ page: {}", pageNum);
        log.info("IN getCurrentUserAppointments ------ size: {}", size);

        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Appointment> page = appointmentService.findByClient(principal.getName(), pageable);
        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();

        return new PageImpl<>(new ModelMapper().map(page.getContent(), listType), page.getPageable(), page.getTotalElements());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseError conflict(DataIntegrityViolationException e) {
        log.warn("IN appointmentController ------ duplicate appointment");
        return new ResponseError("Appointment already exists");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(NoSuchElementException ex) {
        log.warn("IN appointmentController ------ element not found", ex);
        return new ResponseError(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(RuntimeException ex) {
        log.warn("IN appointmentController ----- error occured ", ex);
        return new ResponseError(ex.getMessage());
    }

}
