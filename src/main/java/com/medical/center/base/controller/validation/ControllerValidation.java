package com.medical.center.base.controller.validation;

import com.medical.center.base.controller.alert.ControllerAlert;
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

    public static boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            ControllerAlert.validationAlert(field, true);
            return false;
        }
    }
}
