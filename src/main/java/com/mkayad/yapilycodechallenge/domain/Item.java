package com.mkayad.yapilycodechallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String resourceURI;
    private String name;
}
