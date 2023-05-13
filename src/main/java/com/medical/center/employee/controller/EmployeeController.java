package com.medical.center.employee.controller;

import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.enums.EmployeeType;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient.model.Patient;
import com.medical.center.user.model.User;
import com.medical.center.user.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Controller
public class EmployeeController implements Initializable {

    @FXML
    private Button btnRefresh;

    @FXML
    private ComboBox<EmployeeType> cbEmployeeType;

    @FXML
    private ComboBox<String> cbUser;

    @FXML
    private TableColumn<Employee, Boolean> colEdit;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<?, ?> colEmployeeType;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colStartDate;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private MenuItem deleteUsers;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private Button reset;

    @FXML
    private Button saveEmployee;

    @FXML
    private DatePicker startDate;

    @FXML
    private Label employeeId;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemUsers;

    @FXML
    private MenuItem menuItemPatients;

    @FXML
    private MenuItem menuItemAppointments;

    @FXML
    private MenuItem getEmployeeDetails;

    @FXML
    private MenuItem menuItemTreatmentOutcomes;

    @FXML
    private MenuItem menuItemRooms;

    @FXML
    private MenuItem menuItemAccountings;


    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
    private ObservableList<EmployeeType> employeeTypes = FXCollections.observableArrayList(
        Arrays.stream(EmployeeType.values()).collect(Collectors.toList())
    );

    @FXML
    void menuItemLogout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void menuItemUsers(ActionEvent event) {
        stageManager.switchScene(FxmlView.USER);
    }

    @FXML
    void menuItemPatients(ActionEvent event) {
        stageManager.switchScene(FxmlView.PATIENT);
    }

    @FXML
    void menuItemAppointments(ActionEvent event) {
        stageManager.switchScene(FxmlView.APPOINTMENT);
    }

    @FXML
    void menuItemTreatmentOutcomes(ActionEvent event) {
        stageManager.switchScene(FxmlView.TREATMENT_OUTCOMES);
    }

    @FXML
    void menuItemRooms(ActionEvent event) {
        stageManager.switchScene(FxmlView.ROOM);
    }

    @FXML
    void menuItemAccountings(ActionEvent event) {
        stageManager.switchScene(FxmlView.ACCOUNTING);
    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void refresh(ActionEvent event) {
        loadEmployeeDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadEmployeeDetails();
    }

    @FXML
    void getEmployeeDetails(ActionEvent event) throws IOException {
        Employee employee = employeeTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.EMPLOYEE_VIEW, employee);
    }

    @FXML
    void saveEmployee(ActionEvent event) {
        if (StringUtils.isBlank(employeeId.getText())) {
            if (validateEmployee()) {

                Employee employee = Employee.builder()
                    .firstName(firstName.getText())
                    .lastName(lastName.getText())
                    .employeeType(cbEmployeeType.getSelectionModel().getSelectedItem())
                    .startDate(startDate.getValue())
                    .user(userService.getByEmail(cbUser.getSelectionModel().getSelectedItem()))
                    .build();

                Employee newEmployee = employeeService.create(employee);
                saveAlert(newEmployee);
            }

        } else {
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

        clearFields();
        loadEmployeeDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbEmployeeType.setItems(employeeTypes);
        ObservableList<String> users = FXCollections.observableArrayList(
            userService.getAllWithoutEmployee().stream().map(User::getEmail).collect(Collectors.toList())
        );
        cbUser.setItems(users);
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadEmployeeDetails();
    }

    private void setColumnProperties() {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmployeeType.setCellValueFactory(new PropertyValueFactory<>("employeeType"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEmail.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            if (user == null) {
                return new SimpleStringProperty(null);
            }
            String email = user.getEmail();
            return new SimpleStringProperty(email);
        });
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Employee, Boolean>, TableCell<Employee, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<Employee, Boolean> call(final TableColumn<Employee, Boolean> param) {
                final TableCell<Employee, Boolean> cell = new TableCell<>() {
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
                                Employee employee = getTableView().getItems().get(getIndex());
                                updateEmployee(employee);
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

                    private void updateEmployee(Employee employee) {
                        employeeId.setText(Long.toString(employee.getId()));
                        firstName.setText(employee.getFirstName());
                        lastName.setText(employee.getLastName());
                        cbEmployeeType.getSelectionModel().select(employee.getEmployeeType());
                        startDate.setValue(employee.getStartDate());
                        cbUser.getSelectionModel().select(employee.getUser() != null ? employee.getUser().getEmail() : null);
                    }
                };
                return cell;
            }
        };

    @FXML
    void softDeletedEmployee(ActionEvent event) {
        List<Employee> employees = employeeTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to soft delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
            employees.forEach(it -> employeeService.softDelete(it.getId()));
		}

        clearFields();
        loadEmployeeDetails();
    }

    @FXML
    void hardDeletedEmployee(ActionEvent event) {
        List<Employee> employees = employeeTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to hard delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            employees.forEach(it -> employeeService.hardDelete(it.getId()));
        }

        clearFields();
        loadEmployeeDetails();
    }

    private void loadEmployeeDetails() {
        employeeList.clear();
        employeeList.addAll(employeeService.getAll());
        employeeTable.setItems(employeeList);
        ObservableList<String> users = FXCollections.observableArrayList(
            userService.getAllWithoutEmployee().stream().map(User::getEmail).collect(Collectors.toList())
        );
        cbUser.setItems(users);
    }

    private void clearFields() {
        employeeId.setText(null);
        firstName.clear();
        lastName.clear();
        startDate.getEditor().clear();
        cbEmployeeType.getSelectionModel().clearSelection();
        cbUser.getSelectionModel().clearSelection();
    }

    private void saveAlert(Employee employee) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Employee saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The employee with id " + employee.getId() + " has been created.");
        alert.showAndWait();
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
