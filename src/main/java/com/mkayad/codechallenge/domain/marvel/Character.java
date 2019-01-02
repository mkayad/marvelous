package com.mkayad.codechallenge.domain.marvel;

import lombok.*;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class Character extends Hero {
    private Thumbnail thumbnail;
    private List<URL> urls;
}
