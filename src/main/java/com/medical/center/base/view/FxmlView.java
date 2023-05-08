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
    PATIENT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("patient.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Patient.fxml";
        }
    },
    PATIENT_VIEW {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("patient.details.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/PatientView.fxml";
        }
    },
    APPOINTMENT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("appointment.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Appointment.fxml";
        }
    },
    APPOINTMENT_VIEW {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("appointment.details.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/AppointmentView.fxml";
        }
    },
    TREATMENT_OUTCOMES {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("treatment.outcomes.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/TreatmentOutcomes.fxml";
        }
    },
    TREATMENT_OUTCOMES_VIEW {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("treatment.outcomes.details.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/TreatmentOutcomesView.fxml";
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
