package com.salon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="masters")
public class Master extends User{

    @OneToMany(mappedBy = "master")
    @JsonIgnore
    private List<Appointment> appointments;

    @Column(name="instagram")
    private String instagram;

    @Column(name="image_path")
    private String imagePath;

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "master_service",
            joinColumns = @JoinColumn(name = "master_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    private List<Service> services;
}
