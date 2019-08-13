package com.salon.dto;

import com.salon.entity.Position;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class MasterDTO {

    private Long id;

    @NotBlank(message = "{registration.page.username.error}")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{4,}$",
            message = "{registration.page.username.error}")
    private String username;

    @NotBlank(message = "{registration.page.first.name.en.master.error}")
    @Pattern(regexp = "[A-Z][a-z]*",
            message = "{registration.page.first.name.en.master.error}")
    private String firstName;

    @NotBlank(message = "{registration.page.last.name.en.master.error}")
    @Pattern(regexp = "[A-Z][a-z]*",
            message = "{registration.page.last.name.en.master.error}")
    private String lastName;

    @NotBlank(message = "{registration.page.first.name.ua.master.error}")
    @Pattern(regexp = "[А-ЩЬЮЯЇІЄҐ][а-щьюяїієґ]*",
            message = "{registration.page.first.name.ua.master.error}")
    private String firstNameUa;

    @NotBlank(message = "{registration.page.last.name.ua.master.error}")
    @Pattern(regexp = "[А-ЩЬЮЯЇІЄҐ][а-щьюяїієґ]*",
            message = "{registration.page.last.name.ua.master.error}")
    private String lastNameUa;

    @NotBlank(message = "{registration.page.email.error}")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$",
            message = "{registration.page.email.error}")
    private String email;

    @NotBlank(message = "{registration.page.password.error}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$",
            message = "{registration.page.password.error}")
    private String password;

    @NotBlank(message = "{registration.page.instagram.error}")
    private String instagram;

    @NotNull(message = "{registration.page.position.error}")
    private Position position;

    @NotNull(message = "{registration.page.services.error}")
    @Size(min=1, message = "{registration.page.services.error}")
    private List<Long> services;

}
