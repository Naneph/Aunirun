package com.example.auniruntest3.entity.ResponceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunStandard {
    private long standardId;
    long schoolId;
    int boyOnceTimeMin;
    int boyOnceTimeMax;
    String semesterYear;
}
