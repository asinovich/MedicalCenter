package com.medical.center.base.constant;

public class ControllerConstant {

    public static String EMAIL_REGEX = "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+";
    public static String NAME_REGEX = "^[a-zA-Z0-9_.-]+$";
    public static String PHONE_REGEX = "^\\+\\d{3}-\\d{2}-\\d{7}$";
    public static String TIME_REGEX = "^([01][0-9]|2[0-3]):[0-5][0-9]$";

    public static int PASSWORD_LENGTH = 8;
    public static int NAME_LENGTH = 20;
    public static int ADDRESS_LENGTH = 200;
    public static int NOTE_LENGTH = 500;

    public static int DIAGNOSIS_LENGTH = 70;

    public static int TREATMENT_LENGTH = 1000;
}
