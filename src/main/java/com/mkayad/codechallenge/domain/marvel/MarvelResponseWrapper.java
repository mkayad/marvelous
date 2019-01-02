package com.mkayad.codechallenge.domain.marvel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class MarvelResponseWrapper {
    private int code;
    private String status;
    private CharacterWrapper data;
}

