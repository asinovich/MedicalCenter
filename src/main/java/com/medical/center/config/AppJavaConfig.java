/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medical.center.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppJavaConfig {
	
    @Autowired 
    SpringFXMLLoader springFXMLLoader;

    /**
     * Useful when dumping stack trace to a string for logging.
     * @return ExceptionWriter contains logging utility methods
     */
    @Bean
    @Scope("prototype")
    public ExceptionWriter exceptionWriter() {
        return new ExceptionWriter(new StringWriter());
    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("Bundle");
    }
    
    @Bean
    @Lazy(value = true) //Stage only created after Spring context bootstap
    public StageManager stageManager(Stage stage) throws IOException {
        return new StageManager(springFXMLLoader, stage);
    }

    /**
     * Handles writing exceptions to the Logger Tab and and utility methods needed
     * to facilitate logging of exceptions
     */
    public class ExceptionWriter extends PrintWriter {

        public ExceptionWriter(Writer writer) {
            super(writer);
        }

        private String wrapAroundWithNewlines(String stringWithoutNewlines) {
            return ("\n" + stringWithoutNewlines + "\n");
        }

        /*
         * Convert a stacktrace into a string
         */
        public String getExceptionAsString(Throwable throwable) {
            throwable.printStackTrace(this);

            String exception = super.out.toString();

            return (wrapAroundWithNewlines(exception));
        }
    }
}
