package com.salon.controller;

import com.salon.dto.UserAppointmentDTO;
import com.salon.entity.Appointment;
import com.salon.dto.AppointmentDTO;
import com.salon.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("masters/{masterId}/appointments")
    public List<UserAppointmentDTO> getAllMastersAppointments(@PathVariable Long masterId){

        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();

        return new ModelMapper().map(appointmentService.findAllMastersAppointments(masterId),
                                    listType);
    }

    @GetMapping("masters/{masterId}/appointments/{date}")
    public List<Appointment> getMastersAppointmentsByDate(
            @PathVariable Long masterId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
        log.info("date in get by date ------ {}",date);
        return appointmentService.findAllMastersAppointmentsByDate(masterId, date);
    }

    @GetMapping("/appointments")
    public List<UserAppointmentDTO> getAllAppointments(){

        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();

        return new ModelMapper().map(appointmentService.findAll(), listType);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("appointments")
    public Appointment createAppointment(@RequestBody AppointmentDTO appointment,
                                         Principal principal){
        log.info("Appointment: {} ", appointment);
        return appointmentService.save(appointment, principal.getName());
    }

    @GetMapping("appointments/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long appointmentId){
        try{
            return new ResponseEntity<>( appointmentService.findById(appointmentId) , HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("appointments/{appointmentId}")
    public ResponseEntity deleteAppointment(@PathVariable Long appointmentId){
        appointmentService.deleteById(appointmentId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/me/appointments")
    public List<UserAppointmentDTO> getCurrentUserAppointments(Principal principal){
        List<Appointment> appointments = appointmentService
                                                .findByClient(principal.getName());

        Type listType = new TypeToken<List<UserAppointmentDTO>>() {}.getType();

        return new ModelMapper().map(appointments, listType);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
