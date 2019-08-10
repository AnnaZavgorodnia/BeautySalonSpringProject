package com.salon.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ServiceDTO {

    @NotBlank(message ="{service.name.error}")
    private String name;

    @NotNull(message = "{service.price.error}")
    @Min(value = 0, message = "{service.price.error}")
    private Integer price;
}
