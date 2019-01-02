package com.mkayad.codechallenge.domain.marvel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Thumbnail {
    private String path;
    private String extension;
}
