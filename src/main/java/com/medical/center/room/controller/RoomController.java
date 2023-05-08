package com.medical.center.room.controller;

import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.room.model.Room;
import com.medical.center.room.service.RoomService;
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
public class RoomController implements Initializable {

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<Room, Boolean> colEdit;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private MenuItem delete;

    @FXML
    private TextArea description;

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
    private Label roomId;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private Button save;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private RoomService roomService;

    private ObservableList<Room> roomList = FXCollections.observableArrayList();

    @FXML
    void delete(ActionEvent event) {
        List<Room> rooms = roomTable.getSelectionModel().getSelectedItems();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            rooms.forEach(it -> roomService.delete(it.getId()));
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
        if (StringUtils.isBlank(roomId.getText())) {
            if (validate()) {

                Room room = Room.builder()
                    .name(name.getText())
                    .description(description.getText())
                    .build();

                Room newRoom = roomService.create(room);
                saveAlert(newRoom);
            }

        } else {
            if (validate()) {

                Room room = roomService.getById(Long.parseLong(roomId.getText()));

                room.setName(name.getText());
                room.setDescription(description.getText());

                Room updatedRoom = roomService.update(room);
                updateAlert(updatedRoom);
            }
        }
        clearFields();
        loadDetails();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadDetails();
    }

    private void setColumnProperties() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<Room, Boolean>, TableCell<Room, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<Room, Boolean> call(final TableColumn<Room, Boolean> param) {
                final TableCell<Room, Boolean> cell = new TableCell<>() {
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
                                Room room = getTableView().getItems().get(getIndex());
                                update(room);
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

                    private void update(Room room) {
                        roomId.setText(Long.toString(room.getId()));
                        name.setText(room.getName());
                        description.setText(room.getDescription());
                    }
                };
                return cell;
            }
        };

    private void loadDetails() {
        roomList.clear();
        roomList.addAll(roomService.getAll());
        roomTable.setItems(roomList);
    }

    private void clearFields() {
        roomId.setText(null);
        name.clear();
        description.clear();
    }

    private void saveAlert(Room room) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Room saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The room with id " + room.getId() + " has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Room room) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Room updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The room with id " + room.getId() + " has been updated.");
        alert.showAndWait();
    }

    private boolean validate() {
        return ControllerValidation.validate("name", name.getText(), ControllerConstant.NAME_REGEX)
            && ControllerValidation.validateStringLengthIsNotEmpty("description", description.getText(), ControllerConstant.DESCRIPTION_LENGTH);
    }
}
