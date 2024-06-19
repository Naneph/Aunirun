package com.example.auniruntest3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 路径上的单个点
* */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private int id;
    private String location;
    private int[] edge;
}