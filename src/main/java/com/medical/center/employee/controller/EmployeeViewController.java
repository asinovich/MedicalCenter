package com.medical.center.employee.controller;

import com.medical.center.appointment.model.Appointment;
import com.medical.center.appointment.service.AppointmentService;
import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.enums.EmployeeType;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import com.medical.center.user.service.UserService;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
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
public class EmployeeViewController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private Button back;

    @FXML
    private ComboBox<EmployeeType> cbEmployeeType;

    @FXML
    private ComboBox<String> cbUser;

    @FXML
    private TableColumn<Appointment, String> colAppointmentEmployee;

    @FXML
    private TableColumn<?, ?> colAppointmentId;

    @FXML
    private TableColumn<?, ?> colAppointmentType;

    @FXML
    private TableColumn<?, ?> colNote;

    @FXML
    private TableColumn<?, ?> colRoom;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colVisitDateTime;

    @FXML
    private MenuItem deleteAppointment;

    @FXML
    private Label employeeId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private Button next;

    @FXML
    private Button previous;

    @FXML
    private Button refresh;

    @FXML
    private Button save;

    @FXML
    private DatePicker startDate;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    private LocalDate localDate = LocalDate.now();

    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.EMPLOYEE);
    }

    @FXML
    void getAppointmentDetails(ActionEvent event) {
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.APPOINTMENT_VIEW,  "0 " + employeeId.getText() + " " + appointment.getId());
    }

    @FXML
    void createAppointment(ActionEvent event) {
        stageManager.switchScene(FxmlView.APPOINTMENT_VIEW,  "0 " + employeeId.getText());
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
    void next(ActionEvent event) {
        localDate = localDate.plusDays(1);
        loadAppointmentDetails();
    }

    @FXML
    void previous(ActionEvent event) {
        localDate = localDate.minusDays(1);
        loadAppointmentDetails();
    }

    @FXML
    void refresh(ActionEvent event) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            Stage stage = (Stage) scene.getWindow();
            employeeId.setText(((Employee) stage.getUserData()).getId().toString());
        }

        localDate = LocalDate.now();
        loadDetails();
    }

    @FXML
    void save(ActionEvent event) {
        if (validateEmployee()) {

            Employee employee = employeeService.getById(Long.parseLong(employeeId.getText()));

            employee.setFirstName(firstName.getText());
            employee.setLastName(lastName.getText());
            employee.setEmployeeType(cbEmployeeType.getSelectionModel().getSelectedItem());
            employee.setStartDate(startDate.getValue());
            employee.setUser(userService.getByEmail(cbUser.getSelectionModel().getSelectedItem()));

            Employee updatedEmployee = employeeService.update(employee);
            updateAlert(updatedEmployee);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setColumnProperties();
        loadDetails();
    }

    private void loadDetails() {
        if (StringUtils.isNotBlank(employeeId.getText())) {
            loadAppointmentDetails();

            Employee employee = employeeService.getById(Long.parseLong(employeeId.getText()));
            employeeId.setText(Long.toString(employee.getId()));
            firstName.setText(employee.getFirstName());
            lastName.setText(employee.getLastName());
            cbEmployeeType.getSelectionModel().select(employee.getEmployeeType());
            startDate.setValue(employee.getStartDate());
            cbUser.getSelectionModel().select(employee.getUser() != null ? employee.getUser().getEmail() : null);
        }
    }

    private void loadAppointmentDetails() {
        appointmentList.clear();
        appointmentList.addAll(appointmentService.getByEmployeeIdAndDate(Long.parseLong(employeeId.getText()), localDate));
        appointmentTable.setItems(appointmentList);
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
    }

    private void updateAlert(Employee employee) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Employee updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The employee with id " + employee.getId() + " has been updated.");
        alert.showAndWait();
    }
    private boolean validateEmployee() {
        return ControllerValidation.validateStringLengthIsNotEmpty("first name", firstName.getText(), ControllerConstant.NAME_LENGTH)
            && ControllerValidation.validate("first name", firstName.getText(), ControllerConstant.NAME_REGEX)
            && ControllerValidation.validateStringLengthIsNotEmpty("last name", lastName.getText(), ControllerConstant.NAME_LENGTH)
            && ControllerValidation.validate("last name", firstName.getText(), ControllerConstant.NAME_REGEX)
            && ControllerValidation.isNotBlank("email", cbUser.getSelectionModel().getSelectedItem())
            && ControllerValidation.isNotBlank("start date", startDate.getValue())
            && ControllerValidation.isNotBlank("employee type", cbEmployeeType.getSelectionModel().getSelectedItem());
    }
}
