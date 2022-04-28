package com.project.ksiazeczkazdrowiadlazwierzat.data.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.google.cloud.firestore.annotation.DocumentId;

@NoArgsConstructor
@Accessors(chain = true)
@Getter @Setter
public class AvailableService {

    @DocumentId
    private String id;
    private String name;
    private String description;
}
