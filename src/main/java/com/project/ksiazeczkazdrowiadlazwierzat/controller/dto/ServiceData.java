package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter @Setter
public class ServiceData {

    private String id;
    private String name;
    private String description;
}
