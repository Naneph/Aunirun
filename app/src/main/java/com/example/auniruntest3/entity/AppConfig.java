package com.example.auniruntest3.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppConfig {
    private String phone;
    String password;
    private StringBuffer token = new StringBuffer();

    long distance;
    int runTime;
    String appVersion;
    String brand;
    String deviceToken = "";
    String deviceType = "1";
    String mobileType;
    String sysVersion;



}