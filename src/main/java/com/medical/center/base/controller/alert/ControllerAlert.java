package com.medical.center.base.controller.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControllerAlert {

    public static void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);

        if (empty) {
            alert.setContentText("Please enter " + field);
        } else {
            alert.setContentText("Please enter valid " + field);
        }

        alert.showAndWait();
    }

    public static void validationAlertInValidSize(String field, int size) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("Please enter valid " + field + " with length: " + size);

        alert.showAndWait();
    }
}
