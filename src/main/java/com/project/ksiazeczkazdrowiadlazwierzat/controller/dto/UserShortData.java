package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter @Setter
public class UserShortData {

    private String username;
    private String name;
}
