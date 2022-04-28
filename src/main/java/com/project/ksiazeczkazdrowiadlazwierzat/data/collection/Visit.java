package com.project.ksiazeczkazdrowiadlazwierzat.data.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Accessors(chain = true)
@Getter @Setter
public class Visit {

    private String id;
    private String dateOfVisit;
    private String vet;
    private List<AvailableService> services;

    public List<AvailableService> getServices() {
        if (services == null) {
            return new ArrayList<>();
        }
        return services;
    }
}
