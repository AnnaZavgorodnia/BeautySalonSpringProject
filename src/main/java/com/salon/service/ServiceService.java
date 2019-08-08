package com.salon.service;

import com.salon.entity.Service;
import com.salon.repository.ServiceReporitory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.NotSupportedException;
import java.util.List;
import java.util.Optional;

@Slf4j
@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceReporitory serviceRepo;

    public ServiceService(ServiceReporitory serviceRepo) {
        this.serviceRepo = serviceRepo;
    }

    public Optional<Service> findByName(@NonNull String name){
        return serviceRepo.findServiceByName(name);
    }

    public List<Service> findAll(){
        return serviceRepo.findAll();
    }

}
