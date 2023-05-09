package com.medical.center.user.controller;

import com.medical.center.base.constant.ControllerConstant;
import com.medical.center.base.controller.validation.ControllerValidation;
import com.medical.center.base.view.FxmlView;
import com.medical.center.config.StageManager;
import com.medical.center.employee.model.Employee;
import com.medical.center.user.model.User;
import com.medical.center.user.service.UserService;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

@Controller
public class UserController implements Initializable {

    @FXML
    private Button btnRefresh;

    @FXML
    private TableColumn<User, Boolean> colEdit;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<User, String> colEmployeeFullName;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;


    @FXML
    private Label userId;

    @FXML
    private TableView<User> userTable;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private MenuItem menuItemEmployees;

    @FXML
    private MenuItem menuItemPatients;

    @FXML
    private MenuItem menuItemAppointments;

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
    private UserService userService;

    private ObservableList<User> userList = FXCollections.observableArrayList();

    private static String EMAIL = "email";
    private static String PASSWORD = "password";
    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void menuItemLogout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void menuItemEmployees(ActionEvent event) {
        stageManager.switchScene(FxmlView.EMPLOYEE);
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
    void refresh(ActionEvent event) {
        loadUserDetails();
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
        loadUserDetails();
    }

    @FXML
    void saveUser(ActionEvent event) {
        if (StringUtils.isBlank(userId.getText())) {
            if (ControllerValidation.validate(EMAIL, email.getText(), ControllerConstant.EMAIL_REGEX)
                && ControllerValidation.validateSizeStringIsNotEmpty(PASSWORD, password.getText(), ControllerConstant.PASSWORD_LENGTH)) {

                User user = User.builder()
                    .email(email.getText())
                    .password(password.getText())
                    .build();

                User newUser = userService.create(user);
                saveAlert(newUser);
            }

        } else {
            if (ControllerValidation.validate(EMAIL, email.getText(), ControllerConstant.EMAIL_REGEX)
                && ControllerValidation.validateSizeStringIsNotEmpty(PASSWORD, password.getText(), ControllerConstant.PASSWORD_LENGTH)) {

                User user = userService.getById(Long.parseLong(userId.getText()));

                user.setEmail(email.getText());
                user.setPassword(password.getText());

                User updatedUser = userService.update(user);
                updateAlert(updatedUser);
            }
        }

        clearFields();
        loadUserDetails();
    }

/*    @FXML
    void softDeleteUser(ActionEvent event) {
        User users = userTable.getSelectionModel().getSelectedItems().get(0);

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to soft delete selected?");
        Optional<ButtonType> action = alert.showAndWait();

		if (action.get() == ButtonType.OK) {
			userService.softDelete(users.getId());
		}

        clearFields();
        loadUserDetails();
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnProperties();
        loadUserDetails();
    }

    private void setColumnProperties() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>(EMAIL));
        colPassword.setCellValueFactory(new PropertyValueFactory<>(PASSWORD));
        colEmployeeFullName.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue().getEmployee();
            if (employee == null) {
                return new SimpleStringProperty(null);
            }
            String fullName = employee.getFullName();
            return new SimpleStringProperty(fullName);
        });
        colEdit.setCellFactory(cellFactory);
    }

    Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory =
        new Callback<>() {
            @Override
            public TableCell<User, Boolean> call(final TableColumn<User, Boolean> param) {
                final TableCell<User, Boolean> cell = new TableCell<>() {
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
                                User user = getTableView().getItems().get(getIndex());
                                updateUser(user);
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

                    private void updateUser(User user) {
                        userId.setText(Long.toString(user.getId()));
                        email.setText(user.getEmail());
                        password.setText(user.getPassword());
                    }
                };
                return cell;
            }
        };

    private void loadUserDetails() {
        userList.clear();
        userList.addAll(userService.getAll());
        userTable.setItems(userList);
    }

    private void clearFields() {
        userId.setText(null);
        email.clear();
        password.clear();
    }

    private void saveAlert(User user) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("User saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user with id " + user.getId() + " has been created.");
        alert.showAndWait();
    }

    private void updateAlert(User user) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("User updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The user with id " + user.getId() + " has been updated.");
        alert.showAndWait();
    }

/*
	@FXML
    private Button btnLogout;
	
	@FXML
    private Label userId;
	
	@FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;
    
    @FXML
    private RadioButton rbMale;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rbFemale;
    
    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;
    
    @FXML
    private Button reset;
	
	@FXML
    private Button saveUser;
	
	@FXML
	private TableView<User> userTable;

	@FXML
	private TableColumn<User, Long> colUserId;

	@FXML
	private TableColumn<User, String> colFirstName;

	@FXML
	private TableColumn<User, String> colLastName;

	@FXML
	private TableColumn<User, LocalDate> colDOB;

	@FXML
	private TableColumn<User, String> colGender;
	
	@FXML
    private TableColumn<User, String> colRole;

	@FXML
	private TableColumn<User, String> colEmail;
	
	@FXML
    private TableColumn<User, Boolean> colEdit;
	
	@FXML
    private MenuItem deleteUsers;
	
	@Lazy
    @Autowired
    private StageManager stageManager;
	
	@Autowired
	private UserService userService;
	
	private ObservableList<User> userList = FXCollections.observableArrayList();
	private ObservableList<String> roles = FXCollections.observableArrayList("Admin", "User");
	
	@FXML
	private void exit(ActionEvent event) {
		Platform.exit();
    }

	/**
	 * Logout and go to the login page
	 *
    @FXML
    private void logout(ActionEvent event) throws IOException {
    	stageManager.switchScene(FxmlView.LOGIN);    	
    }
    
    @FXML
    void reset(ActionEvent event) {
    	clearFields();
    }
    
    @FXML
    private void saveUser(ActionEvent event){
    	
    	if(validate("First Name", getFirstName(), "[a-zA-Z]+") &&
    	   validate("Last Name", getLastName(), "[a-zA-Z]+") &&
    	   emptyValidation("DOB", dob.getEditor().getText().isEmpty()) && 
    	   emptyValidation("Role", getRole() == null) ){
    		
    		if(userId.getText() == null || userId.getText() == ""){
    			if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
    				emptyValidation("Password", getPassword().isEmpty())){
    				
    				User user = new User();
        			user.setFirstName(getFirstName());
        			user.setLastName(getLastName());
        			user.setDob(getDob());
        			user.setGender(getGender());
        			user.setRole(getRole());
        			user.setEmail(getEmail());
        			user.setPassword(getPassword());
        			
        			//User newUser = userService.save(user);
        			
        			//saveAlert(newUser);
    			}
    			
    		}else{
    			User user = userService.find(Long.parseLong(userId.getText()));
    			user.setFirstName(getFirstName());
    			user.setLastName(getLastName());
    			user.setDob(getDob());
    			user.setGender(getGender());
    			user.setRole(getRole());
    			User updatedUser =  userService.update(user);
    			updateAlert(updatedUser);
    		}
    		
    		clearFields();
    		loadUserDetails();
    	}
    }
    
    @FXML
    private void deleteUsers(ActionEvent event){
    	List<User> users = userTable.getSelectionModel().getSelectedItems();
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		Optional<ButtonType> action = alert.showAndWait();
		
		//if(action.get() == ButtonType.OK) userService.deleteInBatch(users);
    	
    	loadUserDetails();
    }
    
   	private void clearFields() {
		userId.setText(null);
		firstName.clear();
		lastName.clear();
		dob.getEditor().clear();
		rbMale.setSelected(true);
		rbFemale.setSelected(false);
		cbRole.getSelectionModel().clearSelection();
		email.clear();
		password.clear();
	}
	
	private void saveAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User saved successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been created and \n"+getGenderTitle(user.getGender())+" id is "+ user.getId() +".");
		alert.showAndWait();
	}
	
	private void updateAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been updated.");
		alert.showAndWait();
	}
	
	private String getGenderTitle(String gender){
		return (gender.equals("Male")) ? "his" : "her";
	}

	public String getFirstName() {
		return firstName.getText();
	}

	public String getLastName() {
		return lastName.getText();
	}

	public LocalDate getDob() {
		return dob.getValue();
	}

	public String getGender(){
		return rbMale.isSelected() ? "Male" : "Female";
	}
	
	public String getRole() {
		return cbRole.getSelectionModel().getSelectedItem();
	}

	public String getEmail() {
		return email.getText();
	}

	public String getPassword() {
		return password.getText();
	}
  

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cbRole.setItems(roles);
		
		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setColumnProperties();
		
		// Add all users into table
		loadUserDetails();
	}
	
	
	
	/*
	 *  Set All userTable column properties
	 *
	private void setColumnProperties(){
		/* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));
		
		colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colEdit.setCellFactory(cellFactory);
	}
	
	Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory = 
			new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>()
	{
		@Override
		public TableCell<User, Boolean> call( final TableColumn<User, Boolean> param)
		{
			final TableCell<User, Boolean> cell = new TableCell<User, Boolean>()
			{
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();
				
				@Override
				public void updateItem(Boolean check, boolean empty)
				{
					super.updateItem(check, empty);
					if(empty)
					{
						setGraphic(null);
						setText(null);
					}
					else{
						btnEdit.setOnAction(e ->{
							User user = getTableView().getItems().get(getIndex());
							updateUser(user);
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

				private void updateUser(User user) {
					userId.setText(Long.toString(user.getId()));
					firstName.setText(user.getFirstName());
					lastName.setText(user.getLastName());
					dob.setValue(user.getDob());
					if(user.getGender().equals("Male")) rbMale.setSelected(true);
					else rbFemale.setSelected(true);
					cbRole.getSelectionModel().select(user.getRole());
				}
			};
			return cell;
		}
	};

	
	
	/*
	 *  Add All users to observable list and update table
	 *
	private void loadUserDetails(){
		userList.clear();
		//userList.addAll(userService.findAll());

		userTable.setItems(userList);
	}
	
	/*
	 * Validations
	 *
	private boolean validate(String field, String value, String pattern){
		if(!value.isEmpty()){
			Pattern p = Pattern.compile(pattern);
	        Matcher m = p.matcher(value);
	        if(m.find() && m.group().equals(value)){
	            return true;
	        }else{
	        	validationAlert(field, false);            
	            return false;            
	        }
		}else{
			validationAlert(field, true);            
            return false;
		}        
    }
	
	private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
        	validationAlert(field, true);            
            return false;            
        }
    }	
	
	private void validationAlert(String field, boolean empty){
		Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if(field.equals("Role")) alert.setContentText("Please Select "+ field);
        else{
        	if(empty) alert.setContentText("Please Enter "+ field);
        	else alert.setContentText("Please Enter Valid "+ field);
        }
        alert.showAndWait();
	}
	*/
}
