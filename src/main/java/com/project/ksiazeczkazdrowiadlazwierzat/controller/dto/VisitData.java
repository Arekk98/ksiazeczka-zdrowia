package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Getter @Setter
public class VisitData {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
    private String id;
    private UserShortData vet;
    private List<String> servicesNames;
    private List<String> servicesIds;
}
