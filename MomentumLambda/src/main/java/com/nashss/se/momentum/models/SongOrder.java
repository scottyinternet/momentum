package com.nashss.se.momentum.models;

public class SongOrder {

    public static final String DEFAULT = "DEFAULT";
    public static final String REVERSED = "REVERSED";
    public static final String SHUFFLED = "SHUFFLED";

    private SongOrder() {
    }

    /**
     * Return an array of all the valid SongOrder values.
     * @return An array of SongOrder values.
     */
    public static String[] values() {
        return new String[]{DEFAULT, REVERSED, SHUFFLED};
    }
}
