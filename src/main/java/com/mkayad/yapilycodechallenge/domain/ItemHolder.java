package com.mkayad.yapilycodechallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemHolder {
    private List<Item> items;
    private int available;
    private String collectionURI;
    private int returned;
}
