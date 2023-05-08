package com.medical.center.base.constant;

public class ControllerConstant {

    public static String EMAIL_REGEX = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";
    public static String NAME_REGEX = "^[a-zA-Z0-9_.-]+$";
    public static String PHONE_REGEX = "^\\+\\d{3}-\\d{2}-\\d{7}$";

    public static int PASSWORD_LENGTH = 8;
    public static int NAME_LENGTH = 20;
    public static int ADDRESS_LENGTH = 200;
}
