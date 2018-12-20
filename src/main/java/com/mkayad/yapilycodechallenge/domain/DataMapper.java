package com.mkayad.yapilycodechallenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DataMapper {
    private List<Result> results;
}
