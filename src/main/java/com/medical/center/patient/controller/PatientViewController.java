package com.medical.center.patient.controller;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.service.AppointmentService;
import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import com.medical.center.treatment_outcomes.service.TreatmentOutcomesService;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class PatientViewController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private Button refresh;

    @FXML
    private TableColumn<?, ?> colAppointmentId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDiagnosis;

    @FXML
    private TableColumn<TreatmentOutcomes, String> colTreatmentOutcomesEmployee;

    @FXML
    private TableColumn<?, ?> colNote;

    @FXML
    private TableColumn<?, ?> colRoom;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colTreatment;

    @FXML
    private TableColumn<?, ?> colTreatmentOutcomesId;

    @FXML
    private TableColumn<?, ?> colVisitDateTime;

    @FXML
    private TableColumn<?, ?> colAppointmentType;

    @FXML
    private TableColumn<Appointment, String> colAppointmentEmployee;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private MenuItem deleteTreatmentOutcomes;

    @FXML
    private MenuItem deleteAppointment;

    @FXML
    private MenuItem createTreatmentOutcomes;

    @FXML
    private MenuItem createAppointment;

    @FXML
    private MenuItem getAppointmentDetails;

    @FXML
    private MenuItem getTreatmentOutcomesDetails;

    @FXML
    private DatePicker enrollmentDate;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private Label patientId;

    @FXML
    private TextField address;
    @FXML
    private TextField phone;

    @FXML
    private Button savePatient;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TreatmentOutcomesService treatmentOutcomesService;

    @FXML
    private TableView<TreatmentOutcomes> treatmentOutcomesTable;

    @FXML
    private TableView<Appointment> appointmentTable;

    private ObservableList<TreatmentOutcomes> treatmentOutcomesList = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.PATIENT);
    }

    @FXML
    void getTreatmentOutcomesDetails(ActionEvent event) {
        TreatmentOutcomes treatmentOutcomes = treatmentOutcomesTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.TREATMENT_OUTCOMES_VIEW, patientId.getText() + " " + treatmentOutcomes.getId());
    }

    @FXML
    void createTreatmentOutcomes(ActionEvent event) {
        stageManager.switchScene(FxmlView.TREATMENT_OUTCOMES_VIEW, patientId.getText());
    }

    @FXML
    void deleteTreatmentOutcomes(ActionEvent event) {
        List<TreatmentOutcomes> treatmentOutcomes = treatmentOutcomesTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to hard delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            treatmentOutcomes.forEach(it -> treatmentOutcomesService.delete(it.getId()));
        }

        loadDetails();
    }

    @FXML
    void getAppointmentDetails(ActionEvent event) {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.APPOINTMENT_VIEW, patientId.getText() + " 0 " + appointment.getId());
    }

    @FXML
    void createAppointment(ActionEvent event) {
        stageManager.switchScene(FxmlView.APPOINTMENT_VIEW, patientId.getText() + " 0");
    }

    @FXML
    void deleteAppointment(ActionEvent event) {
        List<Appointment> appointments = appointmentTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            appointments.forEach(it -> appointmentService.delete(it.getId()));
        }

        loadDetails();
    }

    @FXML
    void refresh(ActionEvent event) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            Stage stage = (Stage) scene.getWindow();
            patientId.setText(((Patient) stage.getUserData()).getId().toString());
        }

        loadDetails();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setColumnProperties();
        loadDetails();
    }

    public void setPatientId(Long patient) {
        patientId.setText(patient.toString());
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

    private void loadDetails() {
        if (StringUtils.isNotBlank(patientId.getText())) {
            appointmentList.clear();
            appointmentList.addAll(appointmentService.getByPatientId(Long.parseLong(patientId.getText())));
            appointmentTable.setItems(appointmentList);

            treatmentOutcomesList.clear();
            treatmentOutcomesList.addAll(treatmentOutcomesService.getByPatientId(Long.parseLong(patientId.getText())));
            treatmentOutcomesTable.setItems(treatmentOutcomesList);

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

    private void setColumnProperties() {
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colVisitDateTime.setCellValueFactory(new PropertyValueFactory<>("visitDateTime"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colAppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAppointmentEmployee.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = appointment.getEmployee().getFullName();
            return new SimpleStringProperty(fullName);
        });
        colRoom.setCellValueFactory(new PropertyValueFactory<>("room"));

        colTreatmentOutcomesId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        colTreatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        colTreatmentOutcomesEmployee.setCellValueFactory(cellData -> {
            TreatmentOutcomes treatmentOutcomes = cellData.getValue();
            if (treatmentOutcomes == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = treatmentOutcomes.getEmployee().getFullName();
            return new SimpleStringProperty(fullName);
        });
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
