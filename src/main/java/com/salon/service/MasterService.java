package com.salon.service;

import com.salon.controller.exceptions.NotUniqueUsernameException;
import com.salon.dto.MasterDTO;
import com.salon.entity.Master;
import com.salon.entity.RoleType;
import com.salon.repository.MasterRepository;
import com.salon.repository.ServiceReporitory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MasterService {

    private final MasterRepository masterRepo;
    private final ServiceReporitory serviceRepo;
    private final PasswordEncoder passwordEncoder;

    public MasterService(MasterRepository masterRepo,
                         PasswordEncoder passwordEncoder,
                         ServiceReporitory serviceRepo) {
        this.masterRepo = masterRepo;
        this.passwordEncoder = passwordEncoder;
        this.serviceRepo = serviceRepo;
    }

    @Transactional
    public Master save(@NonNull MasterDTO master){

        Master toSave = new Master();
        toSave.setUsername(master.getUsername());
        toSave.setFullName(master.getFirstName() + " " + master.getLastName());
        toSave.setEmail(master.getEmail());
        toSave.setPassword(passwordEncoder.encode(master.getPassword()));
        toSave.setRole(RoleType.MASTER);
        toSave.setInstagram(master.getInstagram());
        toSave.setPosition(master.getPosition());

        List<com.salon.entity.Service> services = master.getServices().stream()
                            .map(el -> serviceRepo.findById(el)
                                                .orElseThrow(() -> new NotUniqueUsernameException("No such service")))
                            .collect(Collectors.toList());

        toSave.setServices(services);

        return masterRepo.save(toSave);
    }

    public List<Master> getAll(){
        return masterRepo.findAll();
    }


    public Master findById(Long masterId) {
        return masterRepo.findById(masterId)
                .orElseThrow(() -> new NoSuchElementException("master with id:" + masterId +" not found"));
    }

    public Master updateImage(@NotNull Long id, @NotNull String imagePath){
        Master master = masterRepo.findById(id)
                .orElseThrow(()-> new NoSuchElementException("master with id:" + id + " not found"));
        master.setImagePath(imagePath);
        return masterRepo.save(master);
    }

    @Transactional
    public void deleteAll(){
        masterRepo.deleteAll();
    }
}
