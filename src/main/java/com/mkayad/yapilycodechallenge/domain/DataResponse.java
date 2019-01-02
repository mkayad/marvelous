package com.mkayad.yapilycodechallenge.domain;


import java.util.List;

public class DataResponse {
    private List<String> data;

    public DataResponse(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public List<String> getData() {
        return data;
    }
}
