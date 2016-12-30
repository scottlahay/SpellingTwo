package com.scott.spellingdomain;

public class UrlUtil {
    public static final String TEST_SHEET_URL = "1kNpXs13m0nvVZDK71ab4yc8RE5Yv-dMeZ_-gt02gue0";
    public static final String KIDS_SPELLING = "1ZPftQLqmpuw6R2lPpf7khE85MxdJ-uLZ7VPFBHNFJ8g";
    private static final String BASE_URL = "https://docs.google.com/spreadsheets/d/%s/export?format=csv";

    public static String buildUrl(String urlToDownload) {
        return String.format(BASE_URL, urlToDownload);
    }
}
