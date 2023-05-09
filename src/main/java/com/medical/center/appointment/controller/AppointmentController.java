package com.medical.center.appointment.controller;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.service.AppointmentService;
import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.alert.ControllerAlert;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.enums.AppointmentStatus;
import com.medical.center.base.enums.AppointmentType;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import com.medical.center.patient_record.model.PatientRecord;
import com.medical.center.patient_record.service.PatientRecordService;
import com.medical.center.room.model.Room;
import com.medical.center.room.service.RoomService;
import com.medical.center.user.model.User;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class AppointmentController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private Label appointmentId;

    @FXML
    private ComboBox<String> cbEmployee;

    @FXML
    private ComboBox<String> cbPatient;

    @FXML
    private ComboBox<String> cbRoom;

    @FXML
    private ComboBox<AppointmentStatus> cbStatus;

    @FXML
    private ComboBox<AppointmentType> cbType;

    @FXML
    private TableColumn<Appointment, Boolean> colEdit;

    @FXML
    private TableColumn<Appointment, String> colEmployee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNote;

    @FXML
    private TableColumn<Appointment, String> colPatient;

    @FXML
    private TableColumn<Appointment, String> colRoom;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colVisitDateTime;

    @FXML
    private DatePicker date;

    @FXML
    private MenuItem delete;

    @FXML
    private MenuItem getDetails;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemPatients;

    @FXML
    private MenuItem menuItemTreatmentOutcomes;

    @FXML
    private TextArea note;

    @FXML
    private Button refresh;

    @FXML
    private Button reset;

    @FXML
    private Button save;

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

    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    private ObservableList<AppointmentType> appointmentTypes = FXCollections.observableArrayList(
        Arrays.stream(AppointmentType.values()).collect(Collectors.toList())
    );

    private ObservableList<AppointmentStatus> appointmentStatus = FXCollections.observableArrayList(
        Arrays.stream(AppointmentStatus.values()).collect(Collectors.toList())
    );

    @FXML
    void getDetails(ActionEvent event) {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.APPOINTMENT_VIEW, "0 " + appointment.getId());
    }

    @FXML
    void delete(ActionEvent event) {
        List<Appointment> appointments = appointmentTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            appointments.forEach(it -> appointmentService.delete(it.getId()));
        }

        clearFields();
        loadAppointmentDetails();
    }

    @FXML
    void menuItemLogout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void menuItemPatients(ActionEvent event) {
        stageManager.switchScene(FxmlView.PATIENT);
    }

    @FXML
    void menuItemTreatmentOutcomes(ActionEvent event) {
        stageManager.switchScene(FxmlView.TREATMENT_OUTCOMES);
    }


    @FXML
    void refresh(ActionEvent event) {
        loadAppointmentDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadAppointmentDetails();
    }

    @FXML
    void save(ActionEvent event) {
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
            }
        }
        clearFields();
        loadAppointmentDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadAppointmentDetails();
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colVisitDateTime.setCellValueFactory(new PropertyValueFactory<>("visitDateTime"));
        colType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEmployee.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = appointment.getEmployee().getFullName();
            return new SimpleStringProperty(fullName);
        });
        colPatient.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = appointment.getPatientFullName();
            return new SimpleStringProperty(fullName);
        });
        colRoom.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment == null) {
                return new SimpleStringProperty(null);
            }
            String name = appointment.getRoom().getName();
            return new SimpleStringProperty(name);
        });
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Appointment, Boolean>, TableCell<Appointment, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<Appointment, Boolean> call(final TableColumn<Appointment, Boolean> param) {
                final TableCell<Appointment, Boolean> cell = new TableCell<>() {
                    Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                    final Button btnEdit = new Button();

                    @Override
                    public void updateItem(Boolean check, boolean empty) {
                        super.updateItem(check, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btnEdit.setOnAction(e -> {
                                Appointment appointment = getTableView().getItems().get(getIndex());
                                update(appointment);
                            });

                            btnEdit.setStyle("-fx-background-color: transparent;");
                            ImageView iv = new ImageView();
                            iv.setImage(imgEdit);
                            iv.setPreserveRatio(true);
                            iv.setSmooth(true);
                            iv.setCache(true);
                            btnEdit.setGraphic(iv);

                            setGraphic(btnEdit);
                            setAlignment(Pos.CENTER);
                            setText(null);
                        }
                    }

                    private void update(Appointment appointment) {
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
                };
                return cell;
            }
        };

    private void loadAppointmentDetails() {
        appointmentList.clear();
        appointmentList.addAll(appointmentService.getAll());
        appointmentTable.setItems(appointmentList);

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


    private void clearFields() {
        appointmentId.setText(null);
        date.getEditor().clear();
        time.clear();
        note.clear();
        cbEmployee.getSelectionModel().clearSelection();
        cbPatient.getSelectionModel().clearSelection();
        cbStatus.getSelectionModel().clearSelection();
        cbType.getSelectionModel().clearSelection();
        cbRoom.getSelectionModel().clearSelection();
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
