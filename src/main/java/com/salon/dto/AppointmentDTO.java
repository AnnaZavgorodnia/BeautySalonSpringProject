package com.salon.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO{

    @NotNull(message = "{appointment.app.date.error}")
    private LocalDateTime appDate;

    @NotNull(message = "{appointment.app.time.error}")
    private Long master;

    @NotBlank(message = "{appointment.service.name.error}")
    private String serviceName;
}
