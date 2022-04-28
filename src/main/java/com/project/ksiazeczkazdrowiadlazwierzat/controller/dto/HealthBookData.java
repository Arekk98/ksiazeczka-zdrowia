package com.project.ksiazeczkazdrowiadlazwierzat.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Getter
@Setter
public class HealthBookData {
    private AnimalData animal;
    private List<VisitData> visits;
    private List<CommentData> comments;

    private UserPersonalData owner;

}
