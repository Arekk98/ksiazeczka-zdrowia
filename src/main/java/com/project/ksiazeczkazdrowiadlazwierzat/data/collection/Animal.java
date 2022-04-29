package com.project.ksiazeczkazdrowiadlazwierzat.data.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.annotation.DocumentId;

@NoArgsConstructor
@Accessors(chain = true)
@Getter @Setter
public class Animal {

    @DocumentId
    private String id;
    private String name;
    private String sex;
    private String dateOfBirth;
    private String species;
    private String breed;
    private String coat;
    private String typeOfHair;
    private String owner;
    private String imageId;
    private List<Comment> comments;
    private List<Visit> visits;

    public List<Comment> getComments() {
        if (comments == null) {
            return new ArrayList<>();
        }
        return comments;
    }

    public List<Visit> getVisits() {
        if (visits == null) {
            return new ArrayList<>();
        }
        return visits;
    }
}
