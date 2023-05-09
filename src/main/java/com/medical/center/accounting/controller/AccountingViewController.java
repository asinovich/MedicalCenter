package com.medical.center.accounting.controller;

import com.medical.center.accounting.service.AccountingService;
import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.invoice.model.Invoice;
import com.medical.center.invoice.service.InvoiceService;
import com.medical.center.patient.model.Patient;
import java.math.BigDecimal;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class AccountingViewController implements Initializable {

    @FXML
    private TableView<Invoice> invoicesTable;

    @FXML
    private TableColumn<Invoice, Boolean> colEdit;

    @FXML
    private TableColumn<Invoice, String> colEmployee;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNote;

    @FXML
    private TableColumn<?, ?> colTotalCoast;

    @FXML
    private MenuItem delete;

    @FXML
    private ComboBox<String> employee;

    @FXML
    private Label invoiceId;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemUsers;

    @FXML
    private TextArea note;

    @FXML
    private Button refresh;

    @FXML
    private Button reset;

    @FXML
    private Button save;

    @FXML
    private TextField totalCoast;

    @FXML
    private Label totalCoastIncomes;

    @FXML
    private Button back;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private EmployeeService employeeService;

    private String accountingId;

    private ObservableList<Invoice> invoicesList = FXCollections.observableArrayList();

    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.ACCOUNTING);
    }

    @FXML
    void delete(ActionEvent event) {
        List<Invoice> invoices = invoicesTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            invoices.forEach(it -> invoiceService.delete(it.getId()));
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
    void refresh(ActionEvent event) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            // Получаем текущий Stage из сцены
            Stage stage = (Stage) scene.getWindow();
            accountingId = stage.getUserData().toString();
        }

        loadDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadDetails();
    }

    @FXML
    void save(ActionEvent event) {
        if (StringUtils.isBlank(invoiceId.getText())) {
            if (validate()) {

                Invoice invoice = Invoice.builder()
                    .note(note.getText())
                    .totalCoast(new BigDecimal(totalCoast.getText()))
                    .employee(employeeService.getByFullName(employee.getValue()))
                    .accounting(accountingService.getById(Long.parseLong(accountingId)))
                    .build();

                Invoice newInvoice = invoiceService.create(invoice);
                saveAlert(newInvoice);
            }

        } else {
            if (validate()) {

                Invoice invoice = invoiceService.getById(Long.parseLong(invoiceId.getText()));

                invoice.setNote(note.getText());
                invoice.setTotalCoast(new BigDecimal(totalCoast.getText()));
                invoice.setAccounting(accountingService.getById(Long.parseLong(accountingId)));
                invoice.setEmployee(employeeService.getByFullName(employee.getValue()));

                Invoice updatedInvoice = invoiceService.update(invoice);
                updateAlert(updatedInvoice);
            }
        }
        clearFields();
        loadDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Scene scene = refresh.getScene();
        if (scene != null) {
            // Получаем текущий Stage из сцены
            Stage stage = (Stage) scene.getWindow();
            accountingId = stage.getUserData().toString();
        }

        invoicesTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadDetails();
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colTotalCoast.setCellValueFactory(new PropertyValueFactory<>("totalCoast"));
        colEmployee.setCellValueFactory(cellData -> {
            Invoice invoice = cellData.getValue();
            if (invoice == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = invoice.getEmployee().getFullName();
            return new SimpleStringProperty(fullName);
        });
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Invoice, Boolean>, TableCell<Invoice, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<Invoice, Boolean> call(final TableColumn<Invoice, Boolean> param) {
                final TableCell<Invoice, Boolean> cell = new TableCell<>() {
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
                                Invoice invoice = getTableView().getItems().get(getIndex());
                                update(invoice);
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

                    private void update(Invoice invoice) {
                        invoiceId.setText(Long.toString(invoice.getId()));
                        note.setText(invoice.getNote());
                        employee.setValue(invoice.getEmployee().getFullName());
                        totalCoast.setText(invoice.getTotalCoast().toString());

                    }
                };
                return cell;
            }
        };

    private void loadDetails() {
        if (accountingId != null) {
            invoicesList.clear();
            invoicesList.addAll(invoiceService.getByAccountingId(Long.parseLong(accountingId)));
            invoicesTable.setItems(invoicesList);

            totalCoastIncomes.setText(invoiceService.calculateTotalIncomeByAccounting(Long.parseLong(accountingId)).toString());
        }

        ObservableList<String> employees = FXCollections.observableArrayList(
            employeeService.getAll().stream().map(Employee::getFullName).collect(Collectors.toList())
        );
        employee.setItems(employees);
    }


    private void clearFields() {
        invoiceId.setText(null);
        note.clear();
        employee.getSelectionModel().clearSelection();
        totalCoast.clear();
    }

    private void saveAlert(Invoice invoice) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Invoice saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The invoice with id " + invoice.getId() + " has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Invoice invoice) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Invoice updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The invoice with id " + invoice.getId() + " has been updated.");
        alert.showAndWait();
    }

    private boolean validate() {
        return ControllerValidation.validateStringLengthIsNotEmpty("note", note.getText(), ControllerConstant.NOTE_LENGTH)
            && ControllerValidation.isNotBlank("employee", employee.getValue())
            && ControllerValidation.validate("total coast", totalCoast.getText(), ControllerConstant.TOTAL_COAST_REGEX);
    }
}
