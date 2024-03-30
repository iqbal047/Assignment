package com.assingment.spectrumapplication.constants;

public class Constants {

    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String AUTHORITIES_KEY = "roles";
    public static final String ACCESS_TOKEN_NAME = "datkn";
    public static final String REFRESH_TOKEN_NAME = "drtkn";
    public static final String REFRESHED_ACCESS_TOKEN = "refreshed_access_token";
    public static final int ACCESS_TOKEN_DURATION_MILLISECONDS = 30 * 60 * 1000;
    public static final int REFRESH_TOKEN_DURATION_MILLISECONDS = 24 * 60 * 60 * 1000;

    private Constants() {
    }
}
