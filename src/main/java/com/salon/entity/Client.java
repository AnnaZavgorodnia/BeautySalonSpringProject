package com.salon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="clients")
public class Client extends User {

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Appointment> appointments;

}
