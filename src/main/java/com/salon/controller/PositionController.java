package com.salon.controller;

import com.salon.entity.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping(path="/api/positions",
        produces="application/json")
@CrossOrigin(origins="*")
public class PositionController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Position> getAll(){
        return Stream.of(Position.values()).collect(Collectors.toList());
    }

}
