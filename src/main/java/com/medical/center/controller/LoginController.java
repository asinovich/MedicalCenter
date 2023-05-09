package com.medical.center.controller;


import com.medical.center.employee.model.Employee;
import com.medical.center.employee.service.EmployeeService;
import com.medical.center.user.model.User;
import com.medical.center.user.service.UserService;
import java.net.URL;
import java.util.ResourceBundle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.medical.center.config.StageManager;
import com.medical.center.base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
@Slf4j
public class LoginController implements Initializable{

	@FXML
    private Button btnLogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label lblLogin;

    @Autowired
    private UserService userService;

	@Autowired
	private EmployeeService employeeService;

    @Lazy
    @Autowired
    private StageManager stageManager;

	@FXML
    private void login(ActionEvent event) {
    	try {
			userService.login(getUsername(), getPassword());
			log.info("Successful login with email={} and password={}", getUsername(), getPassword());

			Employee employee = employeeService.getByUserId(userService.getByEmail(getUsername()).getId());
			switch (employee.getEmployeeType()) {
				case DOCTOR:
				case STAFF:
					stageManager.switchScene(FxmlView.PATIENT);
					break;
				case ADMIN:
					stageManager.switchScene(FxmlView.USER);
					break;
				case ACCOUNTANT:
					stageManager.switchScene(FxmlView.ACCOUNTING);
					break;
				default:
			}
		} catch (Exception e) {
			log.warn("Failed login with email={} and password={}", getUsername(), getPassword());
			lblLogin.setText("Login Failed.");
    	}
    }

	public String getPassword() {
		return password.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
