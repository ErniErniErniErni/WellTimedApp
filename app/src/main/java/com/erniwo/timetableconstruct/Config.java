package com.erniwo.timetableconstruct;

public class Config {
    private static int MAX_WEEK_NUM = 25; //最大周数
    private static int MAX_CLASS_NUM = 12;

    public static int getMaxWeekNum() {
        return MAX_WEEK_NUM;
    }

    public static int getMaxClassNum() {
        return MAX_CLASS_NUM;
    }
}

