package com.example.placementapp.utils;

public abstract class StringUtils {

    public static boolean isNotBlank(String string)
    {
        return (string!=null) && (string.length()>0);
    }

    public static boolean isBlank(String string)
    {
        return !isNotBlank(string);
    }
}
