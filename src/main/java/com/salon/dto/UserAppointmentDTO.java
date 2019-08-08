package com.salon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.salon.entity.Client;
import com.salon.entity.Master;
import com.salon.entity.Service;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserAppointmentDTO implements Serializable {

    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Master master;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Client client;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Service service;

    private Date appDate;

    private Date appTime;
}
