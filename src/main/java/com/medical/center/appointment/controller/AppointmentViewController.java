package com.medical.center.appointment.controller;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.service.AppointmentService;
import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.enums.AppointmentStatus;
import com.medical.center.base.enums.AppointmentType;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import com.medical.center.patient_record.service.PatientRecordService;
import com.medical.center.room.model.Room;
import com.medical.center.room.service.RoomService;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;

import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Controller
public class AppointmentViewController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private ComboBox<String> cbEmployee;

    @FXML
    private ComboBox<AppointmentStatus> cbStatus;

    @FXML
    private ComboBox<AppointmentType> cbType;

    @FXML
    private ComboBox<String> cbPatient;

    @FXML
    private ComboBox<String> cbRoom;

    @FXML
    private DatePicker date;

    @FXML
    private TextArea note;

    @FXML
    private Label appointmentId;

    @FXML
    private Button refresh;

    @FXML
    private Button saveAppointment;

    @FXML
    private TextField time;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRecordService patientRecordService;

    @Autowired
    private RoomService roomService;

    private String patientId;

    private ObservableList<AppointmentType> appointmentTypes = FXCollections.observableArrayList(
        Arrays.stream(AppointmentType.values()).collect(Collectors.toList())
    );

    private ObservableList<AppointmentStatus> appointmentStatus = FXCollections.observableArrayList(
        Arrays.stream(AppointmentStatus.values()).collect(Collectors.toList())
    );

    @FXML
    void back(ActionEvent event) {
        if (Long.parseLong(patientId) != 0) {
            Patient patient = patientService.getById(Long.parseLong(patientId));
            stageManager.switchScene(FxmlView.PATIENT_VIEW, patient);
        } else {
            stageManager.switchScene(FxmlView.APPOINTMENT);
        }
    }

    @FXML
    void refresh(ActionEvent event) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            // Получаем текущий Stage из сцены
            Stage stage = (Stage) scene.getWindow();
            String[] strings = stage.getUserData().toString().split(" ");

            patientId = strings[0];
            appointmentId.setText(strings.length > 1 ? strings[1] : null);
        }

        loadAppointmentDetails();
    }

    @FXML
    void saveAppointment(ActionEvent event) {
        if (StringUtils.isBlank(appointmentId.getText())) {
            if (validate(false)) {

                Appointment appointment = Appointment.builder()
                    .visitDateTime(LocalDateTime.of(date.getValue(), LocalTime.parse(time.getText())))
                    .note(note.getText())
                    .appointmentType(cbType.getValue())
                    .status(cbStatus.getValue())
                    .patientRecord(patientRecordService.getByPatientId(patientService.getByFullName(cbPatient.getValue()).getId()))
                    .employee(employeeService.getByFullName(cbEmployee.getValue()))
                    .room(roomService.getByName(cbRoom.getValue()))
                    .build();

                Appointment newAppointment = appointmentService.create(appointment);
                saveAlert(newAppointment);
                back(event);
            }

        } else {
            if (validate(true)) {

                Appointment appointment = appointmentService.getById(Long.parseLong(appointmentId.getText()));

                appointment.setVisitDateTime(LocalDateTime.of(date.getValue(), LocalTime.parse(time.getText())));
                appointment.setNote(note.getText());
                appointment.setAppointmentType(cbType.getValue());
                appointment.setStatus(cbStatus.getValue());
                appointment.setPatientRecord(patientRecordService.getByPatientId(patientService.getByFullName(cbPatient.getValue()).getId()));
                appointment.setEmployee(employeeService.getByFullName(cbEmployee.getValue()));
                appointment.setRoom(roomService.getByName(cbRoom.getValue()));

                Appointment updatedAppointment = appointmentService.update(appointment);
                updateAlert(updatedAppointment);
                back(event);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            // Получаем текущий Stage из сцены
            Stage stage = (Stage) scene.getWindow();
            String[] strings = stage.getUserData().toString().split(" ");

            patientId = strings[0];
            appointmentId.setText(strings.length > 1 ? strings[1] : null);
        }
        loadAppointmentDetails();
    }

    private void loadAppointmentDetails() {
        if (StringUtils.isNotBlank(appointmentId.getText())) {
            Appointment appointment = appointmentService.getById(Long.parseLong(appointmentId.getText()));
            appointmentId.setText(Long.toString(appointment.getId()));
            date.setValue(appointment.getVisitDateTime().toLocalDate());
            time.setText(appointment.getVisitDateTime().toLocalTime().toString());
            note.setText(appointment.getNote());
            cbType.getSelectionModel().select(appointment.getAppointmentType());
            cbStatus.getSelectionModel().select(appointment.getStatus());
            cbEmployee.getSelectionModel().select(appointment.getEmployee().getFullName());
            cbPatient.getSelectionModel().select(appointment.getPatientFullName());
            cbRoom.getSelectionModel().select(appointment.getRoom().getName());
        }

        cbType.setItems(appointmentTypes);
        cbStatus.setItems(appointmentStatus);
        ObservableList<String> employees = FXCollections.observableArrayList(
            employeeService.getDoctors().stream().map(Employee::getFullName).collect(Collectors.toList())
        );
        cbEmployee.setItems(employees);
        ObservableList<String> patients = FXCollections.observableArrayList(
            patientService.getAll().stream().map(Patient::getFullName).collect(Collectors.toList())
        );
        cbPatient.setItems(patients);
        ObservableList<String> rooms = FXCollections.observableArrayList(
            roomService.getAll().stream().map(Room::getName).collect(Collectors.toList())
        );
        cbRoom.setItems(rooms);
    }

    private void saveAlert(Appointment appointment) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Appointment saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The appointment with id " + appointment.getId() + " has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Appointment appointment) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Appointment updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The appointment with id " + appointment.getId() + " has been updated.");
        alert.showAndWait();
    }

/*
    private void clearFields() {
        appointmentId.setText(null);
        date.getEditor().clear();
        time.clear();
        note.clear();
        cbType.getSelectionModel().clearSelection();
        cbStatus.getSelectionModel().clearSelection();
        cbEmployee.getSelectionModel().clearSelection();
        cbPatient.getSelectionModel().clearSelection();
        cbRoom.getSelectionModel().clearSelection();
    }
*/

    private boolean validate(boolean isUpdate) {
        return ControllerValidation.isNotBlank("date", date.getValue())
            && ControllerValidation.validate("time", time.getText(), ControllerConstant.TIME_REGEX)
            && ControllerValidation.validateStringLengthIsNotEmpty("note", note.getText(), ControllerConstant.NOTE_LENGTH)
            && ControllerValidation.isNotBlank("appointment type", cbType.getValue())
            && ControllerValidation.isNotBlank("appointment status", cbStatus.getValue())
            && ControllerValidation.isNotBlank("employee", cbEmployee.getValue())
            && ControllerValidation.isNotBlank("patient", cbPatient.getValue())
            && ControllerValidation.isNotBlank("room", cbRoom.getValue())
            && validateAppointment(isUpdate);
    }

    public boolean validateAppointment(boolean isUpdate) {
        Long employeeId = employeeService.getByFullName(cbEmployee.getValue()).getId();
        LocalDateTime visitDateTime = LocalDateTime.of(date.getValue(), LocalTime.parse(time.getText()));
        AppointmentType type = cbType.getValue();
        Long id = isUpdate ? Long.parseLong(appointmentId.getText()) : null;

        if (appointmentService.hasEmployeeFreeTime(employeeId, visitDateTime, type, id)) {
            return true;
        } else {
            validationAppointmentAlert(employeeId, type, visitDateTime, id);
            return false;
        }
    }

    public void validationAppointmentAlert(Long employeeId, AppointmentType type, LocalDateTime localDateTime, Long appointmentId) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);

        alert.setContentText("The employee is busy in  " + localDateTime.toString()
            + "\n Employee has free time: " + appointmentService.getEmployeeFreeTime(employeeId, type, localDateTime.toLocalDate(), appointmentId).toString());

        alert.showAndWait();
    }
}
