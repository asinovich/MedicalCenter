package com.medical.center.accounting.controller;

import com.medical.center.accounting.model.Accounting;
import com.medical.center.accounting.service.AccountingService;
import com.medical.center.appointment.model.Appointment;
import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class AccountingController implements Initializable {

    @FXML
    private Label accountingId;

    @FXML
    private TableView<Accounting> accountingTable;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<Accounting, Boolean> colEdit;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private MenuItem delete;

    @FXML
    private MenuItem getDetails;

    @FXML
    private TextField description;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemUsers;

    @FXML
    private TextField name;

    @FXML
    private Button refresh;

    @FXML
    private Button reset;

    @FXML
    private Button save;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private AccountingService accountingService;

    private ObservableList<Accounting> accountingList = FXCollections.observableArrayList();

    @FXML
    void getDetails(ActionEvent event) {
        Accounting accounting = accountingTable.getSelectionModel().getSelectedItems().get(0);

        stageManager.switchScene(FxmlView.ACCOUNTING_VIEW, accounting.getId());
    }

    @FXML
    void delete(ActionEvent event) {
        List<Accounting> accountings = accountingTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            accountings.forEach(it -> accountingService.delete(it.getId()));
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
        loadDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadDetails();
    }

    @FXML
    void save(ActionEvent event) {
        if (StringUtils.isBlank(accountingId.getText())) {
            if (validate()) {

                Accounting accounting = Accounting.builder()
                    .name(name.getText())
                    .description(description.getText())
                    .build();

                Accounting newAppointment = accountingService.create(accounting);
                saveAlert(newAppointment);
            }

        } else {
            if (validate()) {

                Accounting accounting = accountingService.getById(Long.parseLong(accountingId.getText()));

                accounting.setName(name.getText());
                accounting.setDescription(description.getText());

                Accounting updatedAccounting = accountingService.update(accounting);
                updateAlert(updatedAccounting);
            }
        }
        clearFields();
        loadDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountingTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadDetails();
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Accounting, Boolean>, TableCell<Accounting, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<Accounting, Boolean> call(final TableColumn<Accounting, Boolean> param) {
                final TableCell<Accounting, Boolean> cell = new TableCell<>() {
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
                                Accounting accounting = getTableView().getItems().get(getIndex());
                                update(accounting);
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

                    private void update(Accounting accounting) {
                        accountingId.setText(Long.toString(accounting.getId()));
                        name.setText(accounting.getName());
                        description.setText(accounting.getDescription());

                    }
                };
                return cell;
            }
        };

    private void loadDetails() {
        accountingList.clear();
        accountingList.addAll(accountingService.getAll());
        accountingTable.setItems(accountingList);
    }


    private void clearFields() {
        accountingId.setText(null);
        name.clear();
        description.clear();
    }

    private void saveAlert(Accounting accounting) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Accounting saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The accounting with id " + accounting.getId() + " has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Accounting accounting) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Accounting updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The accounting with id " + accounting.getId() + " has been updated.");
        alert.showAndWait();
    }

    private boolean validate() {
        return ControllerValidation.validate("name", name.getText(), ControllerConstant.NAME_REGEX)
            && ControllerValidation.validateStringLengthIsNotEmpty("description", description.getText(), ControllerConstant.DESCRIPTION_LENGTH);
    }
}
