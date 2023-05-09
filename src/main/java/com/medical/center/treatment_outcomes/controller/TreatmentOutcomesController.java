package com.medical.center.treatment_outcomes.controller;

import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.patient.model.Patient;
import com.medical.center.patient.service.PatientService;
import com.medical.center.patient_record.service.PatientRecordService;
import com.medical.center.treatment_outcomes.model.TreatmentOutcomes;
import com.medical.center.treatment_outcomes.service.TreatmentOutcomesService;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
public class TreatmentOutcomesController implements Initializable {

    @FXML
    private ComboBox<String> cbEmployee;

    @FXML
    private ComboBox<String> cbPatient;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDiagnosis;

    @FXML
    private TableColumn<TreatmentOutcomes, Boolean> colEdit;

    @FXML
    private TableColumn<TreatmentOutcomes, String> colEmployee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<TreatmentOutcomes, String> colPatient;

    @FXML
    private TableColumn<?, ?> colTreatment;

    @FXML
    private DatePicker date;

    @FXML
    private MenuItem delete;

    @FXML
    private MenuItem getDetails;

    @FXML
    private TextField diagnosis;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemUsers;

    @FXML
    private MenuItem menuItemPatients;

    @FXML
    private MenuItem menuItemAppointments;

    @FXML
    private Button refresh;

    @FXML
    private Button reset;

    @FXML
    private Button save;

    @FXML
    private TextArea treatment;

    @FXML
    private Label treatmentOutcomesId;

    @FXML
    private TableView<TreatmentOutcomes> treatmentOutcomesTable;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private TreatmentOutcomesService treatmentOutcomesService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRecordService patientRecordService;


    private ObservableList<TreatmentOutcomes> treatmentOutcomesList = FXCollections.observableArrayList();

    @FXML
    void getDetails(ActionEvent event) {
        TreatmentOutcomes treatmentOutcomes = treatmentOutcomesTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.TREATMENT_OUTCOMES_VIEW, "0 " + treatmentOutcomes.getId());
    }

    @FXML
    void delete(ActionEvent event) {
        List<TreatmentOutcomes> treatmentOutcomes = treatmentOutcomesTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            treatmentOutcomes.forEach(it -> treatmentOutcomesService.delete(it.getId()));
        }

        clearFields();
        loadDetails();
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
    void menuItemPatients(ActionEvent event) {
        stageManager.switchScene(FxmlView.PATIENT);
    }

    @FXML
    void menuItemAppointments(ActionEvent event) {
        stageManager.switchScene(FxmlView.APPOINTMENT);
    }

    @FXML
    void refresh(ActionEvent event) {
        loadDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadDetails();
    }

    @FXML
    void save(ActionEvent event) {
        if (StringUtils.isBlank(treatmentOutcomesId.getText())) {
            if (validate()) {

                TreatmentOutcomes treatmentOutcomes = TreatmentOutcomes.builder()
                    .treatment(treatment.getText())
                    .date(date.getValue())
                    .diagnosis(diagnosis.getText())
                    .patientRecord(patientRecordService.getByPatientId(patientService.getByFullName(cbPatient.getValue()).getId()))
                    .employee(employeeService.getByFullName(cbEmployee.getValue()))
                    .build();

                TreatmentOutcomes updatedTreatmentOutcomes = treatmentOutcomesService.create(treatmentOutcomes);
                saveAlert(updatedTreatmentOutcomes);
            }

        } else {
            if (validate()) {

                TreatmentOutcomes treatmentOutcomes = treatmentOutcomesService.getById(Long.parseLong(treatmentOutcomesId.getText()));

                treatmentOutcomes.setTreatment(treatment.getText());
                treatmentOutcomes.setDiagnosis(diagnosis.getText());
                treatmentOutcomes.setDate(date.getValue());
                treatmentOutcomes.setPatientRecord(patientRecordService.getByPatientId(patientService.getByFullName(cbPatient.getValue()).getId()));
                treatmentOutcomes.setEmployee(employeeService.getByFullName(cbEmployee.getValue()));

                TreatmentOutcomes updatedTreatmentOutcomes = treatmentOutcomesService.update(treatmentOutcomes);
                updateAlert(updatedTreatmentOutcomes);
            }
        }
        clearFields();
        loadDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treatmentOutcomesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadDetails();
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        colTreatment.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        colEmployee.setCellValueFactory(cellData -> {
            TreatmentOutcomes treatmentOutcomes = cellData.getValue();
            if (treatmentOutcomes == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = treatmentOutcomes.getEmployee().getFullName();
            return new SimpleStringProperty(fullName);
        });
        colPatient.setCellValueFactory(cellData -> {
            TreatmentOutcomes treatmentOutcomes = cellData.getValue();
            if (treatmentOutcomes == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = treatmentOutcomes.getPatientFullName();
            return new SimpleStringProperty(fullName);
        });
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<TreatmentOutcomes, Boolean>, TableCell<TreatmentOutcomes, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<TreatmentOutcomes, Boolean> call(final TableColumn<TreatmentOutcomes, Boolean> param) {
                final TableCell<TreatmentOutcomes, Boolean> cell = new TableCell<>() {
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
                                TreatmentOutcomes treatmentOutcomes = getTableView().getItems().get(getIndex());
                                update(treatmentOutcomes);
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

                    private void update(TreatmentOutcomes treatmentOutcomes) {
                        treatmentOutcomesId.setText(Long.toString(treatmentOutcomes.getId()));
                        date.setValue(treatmentOutcomes.getDate());
                        diagnosis.setText(treatmentOutcomes.getDiagnosis());
                        treatment.setText(treatmentOutcomes.getTreatment());
                        cbEmployee.getSelectionModel().select(treatmentOutcomes.getEmployee().getFullName());
                        cbPatient.getSelectionModel().select(treatmentOutcomes.getPatientFullName());
                    }
                };
                return cell;
            }
        };

    private void loadDetails() {
        treatmentOutcomesList.clear();
        treatmentOutcomesList.addAll(treatmentOutcomesService.getAll());
        treatmentOutcomesTable.setItems(treatmentOutcomesList);

        ObservableList<String> employees = FXCollections.observableArrayList(
            employeeService.getAll().stream().map(Employee::getFullName).collect(Collectors.toList())
        );
        cbEmployee.setItems(employees);
        ObservableList<String> patients = FXCollections.observableArrayList(
            patientService.getAll().stream().map(Patient::getFullName).collect(Collectors.toList())
        );
        cbPatient.setItems(patients);
    }


    private void clearFields() {
        treatmentOutcomesId.setText(null);
        date.getEditor().clear();
        diagnosis.clear();
        treatment.clear();
        date.getEditor().clear();
        cbEmployee.getSelectionModel().clearSelection();
        cbPatient.getSelectionModel().clearSelection();
    }

    private void saveAlert(TreatmentOutcomes treatmentOutcomes) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Treatment outcomes saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The treatment outcomes with id " + treatmentOutcomes.getId() + " has been created.");
        alert.showAndWait();
    }

    private void updateAlert(TreatmentOutcomes treatmentOutcomes) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Treatment outcomes updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The treatment outcomes with id " + treatmentOutcomes.getId() + " has been updated.");
        alert.showAndWait();
    }

    private boolean validate() {
        return ControllerValidation.isNotBlank("date", date.getValue())
            && ControllerValidation.validateStringLengthIsNotEmpty("diagnosis", diagnosis.getText(), ControllerConstant.DIAGNOSIS_LENGTH)
            && ControllerValidation.validateStringLengthIsNotEmpty("treatment", treatment.getText(), ControllerConstant.TREATMENT_LENGTH)
            && ControllerValidation.isNotBlank("employee", cbEmployee.getValue())
            && ControllerValidation.isNotBlank("patient", cbPatient.getValue());
    }
}
