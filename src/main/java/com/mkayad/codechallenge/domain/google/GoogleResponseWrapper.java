package com.mkayad.codechallenge.domain.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class GoogleResponseWrapper {
    private TranslationWrapper data;
}

