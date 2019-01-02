package com.mkayad.yapilycodechallenge.domain.marvel;

import com.mkayad.yapilycodechallenge.domain.marvel.Character;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class CharacterWrapper {
    //this variable is called results due to Retrofit requirements in terms of naming convention of the value objects.
    private List<Character> results;
}
