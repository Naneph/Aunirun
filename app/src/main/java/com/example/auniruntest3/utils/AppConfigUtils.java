package com.example.auniruntest3.utils;

import com.example.auniruntest3.entity.AppConfig;

import java.util.Random;

public class AppConfigUtils {

    private static final Random rand = new Random();

    public static AppConfig getLoginConfig(String phone, String password) {
        AppConfig appConfig = new AppConfig();
        appConfig.setPhone(phone);
        appConfig.setPassword(password);
        appConfig.setAppVersion("1.8.0");
        appConfig.setBrand(SystemUtil.getDeviceBrand());
        appConfig.setMobileType(SystemUtil.getSystemModel());
        appConfig.setSysVersion(SystemUtil.getSystemVersion());
        return appConfig;
    }


    //10千米级
    public static AppConfig getRunConfig10(String phone, String password) {
        AppConfig appConfig = getLoginConfig(phone, password);
        long distance = 10000;
        //获取随机距离
        long finalDistance = getDistance(distance, distance + 4999);
        appConfig.setDistance(finalDistance);

        //设置相匹配的时间
        appConfig.setRunTime(getTime(finalDistance));

        return appConfig;
    }




    //5千米级
    public static AppConfig getRunConfig5(String phone, String password) {
        AppConfig appConfig = getLoginConfig(phone, password);
        long distance = 5000;
        long finalDistance = getDistance(distance, distance + 4999);
        appConfig.setDistance(finalDistance);
        appConfig.setRunTime(getTime(finalDistance));
        return appConfig;
    }

    //4千米级
    public static AppConfig getRunConfig4(String phone, String password) {
        AppConfig appConfig = getLoginConfig(phone, password);
        long distance = 4000;
        long finalDistance = getDistance(distance, distance + 999);
        appConfig.setDistance(finalDistance);
        appConfig.setRunTime(getTime(finalDistance));
        return appConfig;
    }

    //3千米级
    public static AppConfig getRunConfig3(String phone, String password) {
        AppConfig appConfig = getLoginConfig(phone, password);
        long distance = 3000;
        long finalDistance = getDistance(distance, distance + 999);
        appConfig.setDistance(finalDistance);
        appConfig.setRunTime(getTime(finalDistance));
        return appConfig;
    }


    //1千米级
    public static AppConfig getRunConfig(String phone, String password) {
        AppConfig appConfig = getLoginConfig(phone, password);
        long distance = 1000;
        long finalDistance = getDistance(distance, distance + 999);
        appConfig.setDistance(finalDistance);
        appConfig.setRunTime(getTime(finalDistance));
        return appConfig;
    }










    private static Long getDistance(long start, long end) {
        Random rand = new Random();
        long distance = rand.nextInt((int) (end - start) + 1) + start;
        return distance;
    }
    private static int getTime(long distance) {

        double mean = 7.5 * distance;
        double stdDev = (9 * distance - 6 * distance) / 3.29; // 99% 的数据在 mean ± 3.29*stdDev 之间
        double time = (mean + stdDev * rand.nextGaussian());

        //防止配速小于6 min/km
        while (time / distance < 6.1) {
            time = (mean + stdDev * rand.nextGaussian());
        }
        return (int) Math.ceil(time / 1000);
    }
}
