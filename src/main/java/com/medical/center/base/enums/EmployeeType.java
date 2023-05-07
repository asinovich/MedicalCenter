package com.medical.center.base.enums;

public enum EmployeeType {
    ADMIN,
    DOCTOR,
    ACCOUNTANT,
    STAFF;

    public static boolean validateFromString(String value) {
        try {
            EmployeeType.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
