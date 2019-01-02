package com.mkayad.codechallenge.domain.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class TranslationWrapper {
    private List<Translation> translations;
}
