package com.example.airplains.utils;

public class DestinationUtils {
    public static boolean isAirportCode(String str) {
        return str.length() == 3 && str.chars().allMatch(Character::isUpperCase);
    }
}
