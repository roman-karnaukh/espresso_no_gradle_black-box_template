package com.espresso.black_box.environment.data;

public class Consts {

    public static class Servers{
        public final static String REQUEST_FILTER = "REQUEST_FILTER";
        public final static String RESPONSE_FILTER = "RESPONSE_FILTER";
        public final static String API_URL_TEST = "API_URL_TEST";
        public final static String API_URL_REAL = "API_URL_REAL";
        public final static String API_URL_BEFORE_REAL = "API_URL_BEFORE_REAL";
    }

    public static class OtherData {
        public static int currentVersionCode;
        public static int oldVerCode = 000;
    }
}