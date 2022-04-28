package com.project.ksiazeczkazdrowiadlazwierzat.data.collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(chain = true)
@Getter @Setter
public class Comment {

    private String id;
    private String date;
    private String value;
}
