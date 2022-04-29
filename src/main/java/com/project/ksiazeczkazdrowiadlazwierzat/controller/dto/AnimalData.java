package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Accessors(chain = true)
@Getter @Setter
public class AnimalData {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    private String id;
    private String name;
    private String species;
    private String breed;
    private String sex;
    private String coat;
    private String typeOfHair;
    private String ownerUsername;
    private String imageId;
}
