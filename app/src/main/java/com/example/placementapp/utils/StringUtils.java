package com.example.placementapp.utils;

public abstract class StringUtils {
    public static boolean isNotBlank(String string)
    {
        return (string==null) || (string.isEmpty());
    }
}
