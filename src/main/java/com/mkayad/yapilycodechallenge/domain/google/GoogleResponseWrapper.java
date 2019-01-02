package com.mkayad.yapilycodechallenge.domain.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class GoogleResponseWrapper {
    private TranslationWrapper data;
}

