package com.medical.center.patient.controller;

import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
public class PatientController implements Initializable {

    @FXML
    private TextField address;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colDateOfBirth;

    @FXML
    private TableColumn<Patient, Boolean> colEdit;

    @FXML
    private TableColumn<?, ?> colEnrollmentDate;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colPatientd;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private DatePicker enrollmentDate;

    @FXML
    private TextField firstName;

    @FXML
    private MenuItem hardDeletedPatient;

    @FXML
    private MenuItem softDeletedPatient;

    @FXML
    private MenuItem getPatientDetails;

    @FXML
    private TextField lastName;

    @FXML
    private MenuItem menuItemEmployees;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemUsers;

    @FXML
    private Label patientId;

    @FXML
    private TextField phone;

    @FXML
    private Button reset;

    @FXML
    private Button savePatient;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private PatientService patientService;

    //@Autowired
    //private PatientViewController patientViewController;

    private Stage stage;
    private Scene scene;
    private Parent root;


    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        patientTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadPatientDetails();
    }

    private void setColumnProperties() {
        colPatientd.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colEnrollmentDate.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEdit.setCellFactory(cellFactoryEdit);
    }

    Callback<TableColumn<Patient, Boolean>, TableCell<Patient, Boolean>> cellFactoryEdit =
        new Callback<>() {
            @Override
            public TableCell<Patient, Boolean> call(final TableColumn<Patient, Boolean> param) {
                final TableCell<Patient, Boolean> cell = new TableCell<>() {
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
                                Patient patient = getTableView().getItems().get(getIndex());
                                updatePatient(patient);
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

                    private void updatePatient(Patient patient) {
                        patientId.setText(Long.toString(patient.getId()));
                        firstName.setText(patient.getFirstName());
                        lastName.setText(patient.getLastName());
                        dateOfBirth.setValue(patient.getDateOfBirth());
                        enrollmentDate.setValue(patient.getEnrollmentDate());
                        phone.setText(patient.getPhone());
                        address.setText(patient.getAddress());
                    }
                };
                return cell;
            }
        };

    @FXML
    void softDeletedPatient(ActionEvent event) {
        List<Patient> patients = patientTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to soft delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            patients.forEach(it -> patientService.softDelete(it.getId()));
        }

        clearFields();
        loadPatientDetails();
    }

    @FXML
    void hardDeletedPatient(ActionEvent event) {
        List<Patient> patients = patientTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to hard delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            patients.forEach(it -> patientService.hardDelete(it.getId()));
        }

        clearFields();
        loadPatientDetails();
    }

    @FXML
    void getPatientDetails(ActionEvent event) throws IOException {
        Patient patient = patientTable.getSelectionModel().getSelectedItems().get(0);
/*        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PatientView.fxml"));
        root = loader.load();

        //patientViewController.setPatientId(patient.getId());

        //root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
        stage = new Stage();
        stage.setUserData(patient.getId().toString());
        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/


        stageManager.switchScene(FxmlView.PATIENT_VIEW, patient);
    }

    @FXML
    void menuItemLogout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void menuItemUsers(ActionEvent event) {
        stageManager.switchScene(FxmlView.USER);
    }

    @FXML
    void refresh(ActionEvent event) {
        loadPatientDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadPatientDetails();
    }

    @FXML
    void savePatient(ActionEvent event) {
        if (StringUtils.isBlank(patientId.getText())) {
            if (validatePatient()) {

                Patient patient = Patient.builder()
                    .firstName(firstName.getText())
                    .lastName(lastName.getText())
                    .dateOfBirth(dateOfBirth.getValue())
                    .enrollmentDate(enrollmentDate.getValue())
                    .phone(phone.getText())
                    .address(address.getText())
                    .build();

                Patient newPatient = patientService.create(patient);
                saveAlert(newPatient);
            }

        } else {
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

        clearFields();
        loadPatientDetails();
    }

    private void loadPatientDetails() {
        patientList.clear();
        patientList.addAll(patientService.getAll());
        patientTable.setItems(patientList);
    }

    private void clearFields() {
        patientId.setText(null);
        firstName.clear();
        lastName.clear();
        dateOfBirth.getEditor().clear();
        enrollmentDate.getEditor().clear();
        phone.clear();
        address.clear();
    }

    private void saveAlert(Patient patient) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Patient saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The patient with id " + patient.getId() + " has been created.");
        alert.showAndWait();
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
