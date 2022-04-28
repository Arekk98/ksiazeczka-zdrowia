package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Registration {

    private String username;
    private String password;
    private String repeatedPassword;
    private String vetCode;
    private String name;
    private String surname;
}
