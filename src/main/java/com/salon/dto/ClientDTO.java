package com.salon.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ClientDTO {

    @NotBlank(message = "{registration.page.username.error}")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{4,}$",
            message = "{registration.page.username.error}")
    private String username;

    @NotBlank(message = "{registration.page.first.name.error}")
    private String firstName;

    @NotBlank(message = "{registration.page.last.name.error}")
    private String lastName;

    @NotBlank(message = "{registration.page.email.error}")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$",
            message = "{registration.page.email.error}")
    private String email;

    @NotBlank(message = "{registration.page.password.error}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$",
            message = "{registration.page.password.error}")
    private String password;

}
