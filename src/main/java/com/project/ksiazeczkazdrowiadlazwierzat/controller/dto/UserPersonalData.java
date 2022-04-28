package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter @Setter
public class UserPersonalData {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String street;
    private String city;
    private String postcode;
}
