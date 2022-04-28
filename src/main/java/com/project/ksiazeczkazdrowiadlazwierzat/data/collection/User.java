package com.project.ksiazeczkazdrowiadlazwierzat.data.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import com.google.cloud.firestore.annotation.DocumentId;

@NoArgsConstructor
@Accessors(chain = true)
@Getter @Setter
public class User {

    @DocumentId
    private String id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String street;
    private String city;
    private String postcode;
    private String email;
    private String username;
    private String password;
    private String role;
}
