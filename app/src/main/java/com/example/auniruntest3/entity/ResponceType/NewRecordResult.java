package com.example.auniruntest3.entity.ResponceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewRecordResult {
    String resultStatus;
    String resultDesc;
    String overSpeedWarn;
    String warnContent;
    long recordId;
}