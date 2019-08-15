package com.salon.repository;

import com.salon.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByMasterId(Long masterId);
    List<Appointment> findAppointmentsByMasterIdAndAppDate(Long masterId, Date appDate);
    Page<Appointment> findAppointmentsByClient_UsernameOrderByAppDateAscAppTimeAsc(String username, Pageable pageable);
}
