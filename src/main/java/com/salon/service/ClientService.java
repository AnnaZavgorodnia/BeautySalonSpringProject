package com.salon.service;

import com.salon.controller.exception.NotUniqueUsernameException;
import com.salon.dto.ClientDTO;
import com.salon.entity.Client;
import com.salon.entity.RoleType;
import com.salon.repository.ClientRepository;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepo;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepo,
                         PasswordEncoder passwordEncoder) {
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Client save(@NonNull ClientDTO client){

        Client toSave = new Client();
        toSave.setUsername(client.getUsername());
        toSave.setFullName(client.getFirstName() + " " + client.getLastName());
        toSave.setEmail(client.getEmail());
        toSave.setPassword(passwordEncoder.encode(client.getPassword()));
        toSave.setRole(RoleType.CLIENT);

        return clientRepo.save(toSave);
    }

    public List<Client> getAll(){
        return clientRepo.findAll();
    }

    public Client findById(Long clientId) {
        return clientRepo.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("User with id=" + clientId + " not found"));
    }

    public Optional<Client> findByUsername(String clientUsername) {
        return clientRepo.findByUsername(clientUsername);
    }

    @Transactional
    public void deleteAll(){
        clientRepo.deleteAll();
    }
}
