package com.mkayad.yapilycodechallenge.domain.marvel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
abstract public class Hero {
    private int id;
    private String name;
    private String description;
}
