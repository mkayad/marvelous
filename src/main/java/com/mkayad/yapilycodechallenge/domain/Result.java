package com.mkayad.yapilycodechallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Result {
    private int id;
    private String name;
    private String resourceURI;
    private ItemHolder comics;
    private ItemHolder series;
    private ItemHolder stories;
    private ItemHolder events;
    private List<URLHolder> urls;
}
