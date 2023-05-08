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
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class TreatmentOutcomesViewController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private ComboBox<String> cbEmployee;

    @FXML
    private ComboBox<String> cbPatient;

    @FXML
    private DatePicker date;

    @FXML
    private TextField diagnosis;

    @FXML
    private TextArea treatment;

    @FXML
    private Button refresh;

    @FXML
    private Button save;

    @FXML
    private Label treatmentOutcomesId;

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

    private String patientId;

    @FXML
    void back(ActionEvent event) {
        if (Long.parseLong(patientId) != 0) {
            Patient patient = patientService.getById(Long.parseLong(patientId));
            stageManager.switchScene(FxmlView.PATIENT_VIEW, patient);
        } else {
            stageManager.switchScene(FxmlView.TREATMENT_OUTCOMES);
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
            treatmentOutcomesId.setText(strings.length > 1 ? strings[1] : null);
        }

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
                back(event);
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
            treatment.setText(strings.length > 1 ? strings[1] : null);
        }
        loadDetails();
    }

    private void loadDetails() {
        if (StringUtils.isNotBlank(treatmentOutcomesId.getText())) {
            TreatmentOutcomes treatmentOutcomes = treatmentOutcomesService.getById(Long.parseLong(treatmentOutcomesId.getText()));
            treatmentOutcomesId.setText(Long.toString(treatmentOutcomes.getId()));
            date.setValue(treatmentOutcomes.getDate());
            diagnosis.setText(treatmentOutcomes.getDiagnosis());
            treatment.setText(treatmentOutcomes.getTreatment());
            cbEmployee.getSelectionModel().select(treatmentOutcomes.getEmployee().getFullName());
            cbPatient.getSelectionModel().select(treatmentOutcomes.getPatientFullName());
        }

        ObservableList<String> employees = FXCollections.observableArrayList(
            employeeService.getAll().stream().map(Employee::getFullName).collect(Collectors.toList())
        );
        cbEmployee.setItems(employees);
        ObservableList<String> patients = FXCollections.observableArrayList(
            patientService.getAll().stream().map(Patient::getFullName).collect(Collectors.toList())
        );
        cbPatient.setItems(patients);
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
