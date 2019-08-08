package com.salon.repository;

import com.salon.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceReporitory extends JpaRepository<Service, Long>{
    Optional<Service> findServiceByName(String name);
}
