package com.salon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "services")
public class Service{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_ua", nullable = false)
    private String nameUa;

    @Column(name = "price", nullable = false)
    private Integer price;

    @ManyToMany(mappedBy = "services", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Master> masters = new ArrayList<>();

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    private List<Appointment> appointments;

}
