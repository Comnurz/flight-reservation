package com.example.flightreservation.models.responses;

import java.util.List;

public class ListResponse<T> {
    private List<T> list;
    private Integer status;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
