package com.mkayad.codechallenge.domain.marvel;

import lombok.Getter;
import lombok.ToString;

/**
 * This class is used for returing a subset of character fields
 */
@ToString
@Getter
public class SimpleCharacter extends Hero {
    private Thumbnail thumbnail;
    public SimpleCharacter(int id,String name,String description,Thumbnail thumbnail){
        super(id,name,description);
        this.thumbnail=thumbnail;
    }
}
