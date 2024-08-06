package com.reve.authentication.server.utils;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
    private static final String API_STRING="";

    public static String getCookiesPath(HttpServletRequest request) {
         return request.getContextPath()+API_STRING;
    }

}
