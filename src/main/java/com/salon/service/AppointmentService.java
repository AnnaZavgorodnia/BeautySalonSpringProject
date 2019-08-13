package com.salon.service;

import com.salon.entity.Appointment;
import com.salon.dto.AppointmentDTO;
import com.salon.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class AppointmentService {

    private final AppointmentRepository appRepo;
    private final MasterService masterService;
    private final ClientService clientService;
    private final ServiceService serviceService;

    public AppointmentService(AppointmentRepository appRepo,
                              MasterService masterService,
                              ClientService clientService,
                              ServiceService serviceService) {
        this.appRepo = appRepo;
        this.masterService = masterService;
        this.clientService = clientService;
        this.serviceService = serviceService;
    }

    public List<Appointment> findAllMastersAppointments(Long masterId) {
        return appRepo.findByMasterId(masterId);
    }

    public Appointment save(AppointmentDTO appointment, String clientUsername) {

        LocalDateTime localDate = LocalDateTime.from(appointment.getAppDate());
        localDate.withHour(0);

        Date time = Date
                .from(appointment.getAppDate().atZone(ZoneId.systemDefault())
                        .toInstant());
        Date date = Date.from(localDate.atZone(ZoneId.systemDefault())
                .toInstant());

        Appointment app = Appointment.builder()
                            .appDate(date)
                            .appTime(time)
                            .client(clientService.findByUsername(clientUsername)
                                    .orElseThrow(() -> new NoSuchElementException("user with username: " + clientUsername + " not found")))
                            .master(masterService.findById(appointment.getMaster()))
                            .service(serviceService.findByName(appointment.getServiceName())
                                    .orElseThrow(() -> new NoSuchElementException("service: " + appointment.getServiceName() + " not found")))
                            .build();
        return appRepo.save(app);
    }

    public Appointment findById(Long appId){
        return appRepo.findById(appId)
                .orElseThrow(() -> new NoSuchElementException("No appointments with id: " + appId));
    }

    public List<Appointment> findAllMastersAppointmentsByDate(Long masterId, LocalDateTime date) {
        Date parsedDate = Date
                .from(date.atZone(ZoneId.systemDefault())
                        .toInstant());
        return appRepo.findAppointmentsByMasterIdAndAppDate(masterId, parsedDate);
    }

    public Page<Appointment> findByClient(String name, Pageable pageable) {
        return appRepo.findAppointmentsByClient_UsernameOrderByAppDateAscAppTimeAsc(name, pageable);
    }

    public void deleteById(Long appointmentId) {
        appRepo.deleteById(appointmentId);
    }

    public List<Appointment> findAll() {
        return appRepo.findAll();
    }

    public void deleteAll(){
        appRepo.deleteAll();
    }
}
