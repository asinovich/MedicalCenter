package com.medical.center.base.controller.validation;

import com.medical.center.base.controller.alert.ControllerAlert;
import com.medical.center.base.enums.AppointmentStatus;
import com.medical.center.base.enums.AppointmentType;
import com.medical.center.base.enums.EmployeeType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class ControllerValidation {

    public static boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                ControllerAlert.validationAlert(field, false);
                return false;
            }
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }

    public static boolean validateSizeStringIsNotEmpty(String field, String value, int size) {
        if (!value.isEmpty()) {

            if (StringUtils.length(value) >=  size) {
                return true;
            } else {
                ControllerAlert.validationAlertInValidSize(field, size);
                return false;
            }
        } else {
            ControllerAlert.validationAlertInValidSize(field, size);
            return false;
        }
    }

    public static boolean validateStringLengthIsNotEmpty(String field, String value, int size) {
        if (!value.isEmpty()) {

            if (StringUtils.length(value) > 1 || StringUtils.length(value) < size) {
                return true;
            } else {
                ControllerAlert.validationAlertInValidSize(field, size);
                return false;
            }
        } else {
            ControllerAlert.validationAlertInValidSize(field, size);
            return false;
        }
    }

    public static boolean validateEmployeeTypeNotEmpty(String field, String value) {
        if (StringUtils.isBlank(value)) {
            ControllerAlert.validationAlert(field, true);
            return false;
        }

        if (!EmployeeType.validateFromString(value)) {
            ControllerAlert.validationAlert(field, true);
            return false;
        }

        return true;
    }

    public static boolean isNotBlank(String field, String value) {
        if (StringUtils.isNotBlank(value)) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }

    public static boolean isNotBlank(String field, LocalDate value) {
        if (value != null) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }

    public static boolean isNotBlank(String field, EmployeeType value) {
        if (value != null) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }

    public static boolean isNotBlank(String field, AppointmentType value) {
        if (value != null) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }

    public static boolean isNotBlank(String field, AppointmentStatus value) {
        if (value != null) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }

    public static boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }
}
