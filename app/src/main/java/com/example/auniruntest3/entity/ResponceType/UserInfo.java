package com.example.auniruntest3.entity.ResponceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    long userId;
    long studentId;
    String registerCode;
    String studentName;
    String gender;
    long schoolId;
    String schoolName;
    long classId;
    int studentClass;
    String className;
    int startSchool;
    String collegeCode;
    String collegeName;
    String majorCode;
    String majorName;
    String nationCode;
    String birthday;
    String idCardNo;
    String addrDetail;
    String studentSource;
    String userVerifyStatus;
    OAuth oauthToken;
    @Data
    public static class OAuth{
        String refreshToken;
        String token;
    }
}
