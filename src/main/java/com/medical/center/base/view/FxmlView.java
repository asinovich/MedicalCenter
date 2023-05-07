package com.medical.center.base.view;

import java.util.ResourceBundle;

public enum FxmlView {

    USER {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("user.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/User.fxml";
        }
    },
    EMPLOYEE {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("employee.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Employee.fxml";
        }
    },
    LOGIN {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/Login.fxml";
        }
    };
    
    public abstract String getTitle();
    public abstract String getFxmlFile();
    
    static String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
