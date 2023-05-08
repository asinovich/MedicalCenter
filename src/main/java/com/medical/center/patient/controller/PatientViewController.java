package com.medical.center.patient.controller;

import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import com.medical.center.patient.service.impl.PatientServiceImpl;
import com.medical.center.user.model.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class PatientViewController implements Initializable {


    @FXML
    private TextArea address;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private DatePicker enrollmentDate;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemUsers;

    @FXML
    private Label patientId;

    @FXML
    private TextField phone;

    @FXML
    private Button refresh;

    @FXML
    private Button savePatient;

    @FXML
    private Button back;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private PatientService patientService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEmployeeDetails();
    }

    public void setPatientId(Long patient) {
        patientId.setText(patient.toString());
    }

    @FXML
    void menuItemLogout(ActionEvent event) {

    }

    @FXML
    void menuItemUsers(ActionEvent event) {

    }

    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.PATIENT);
    }

    @FXML
    void refresh(ActionEvent event) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            // Получаем текущий Stage из сцены
            Stage stage = (Stage) scene.getWindow();
            patientId.setText (((Patient) stage.getUserData()).getId().toString());
        }

        loadEmployeeDetails();
    }

    @FXML
    void savePatient(ActionEvent event) {
        if (validatePatient()) {

            Patient patient = patientService.getById(Long.parseLong(patientId.getText()));

            patient.setFirstName(firstName.getText());
            patient.setLastName(lastName.getText());
            patient.setDateOfBirth(dateOfBirth.getValue());
            patient.setEnrollmentDate(enrollmentDate.getValue());
            patient.setPhone(phone.getText());
            patient.setAddress(address.getText());

            Patient updatedPatient = patientService.update(patient);
            updateAlert(updatedPatient);
        }
    }

    private void loadEmployeeDetails() {
        if (StringUtils.isNotBlank(patientId.getText())) {
            Patient patient = patientService.getById(Long.parseLong(patientId.getText()));
            patientId.setText(Long.toString(patient.getId()));
            firstName.setText(patient.getFirstName());
            lastName.setText(patient.getLastName());
            dateOfBirth.setValue(patient.getDateOfBirth());
            enrollmentDate.setValue(patient.getEnrollmentDate());
            phone.setText(patient.getPhone());
            address.setText(patient.getAddress());
        }
    }

    private void updateAlert(Patient patient) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Patient updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The patient with id " + patient.getId() + " has been updated.");
        alert.showAndWait();
    }

    private boolean validatePatient() {
        return ControllerValidation.validateStringLengthIsNotEmpty("first name", firstName.getText(), ControllerConstant.NAME_LENGTH)
            && ControllerValidation.validate("first name", firstName.getText(), ControllerConstant.NAME_REGEX)
            && ControllerValidation.validateStringLengthIsNotEmpty("last name", lastName.getText(), ControllerConstant.NAME_LENGTH)
            && ControllerValidation.validate("last name", firstName.getText(), ControllerConstant.NAME_REGEX)
            && ControllerValidation.isNotBlank("date of birth", dateOfBirth.getValue())
            && ControllerValidation.isNotBlank("enrollment date", enrollmentDate.getValue())
            && ControllerValidation.validate("phone", phone.getText(), ControllerConstant.PHONE_REGEX)
            && ControllerValidation.validateStringLengthIsNotEmpty("address", address.getText(), ControllerConstant.ADDRESS_LENGTH);
    }
}
