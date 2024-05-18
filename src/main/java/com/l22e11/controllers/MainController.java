package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import com.l22e11.helper.AccountWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController implements Initializable{

    @FXML
    private TextField newCategory, newDescription;
    @FXML
    private Label SuccessfullCategoryRegister;
    
    // private int tabFocused = 0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }

    @FXML
    private void addCategory(ActionEvent event) {
        String category = newCategory.getText();
        String description = newDescription.getText();
        boolean isOk = AccountWrapper.registerCategory(category, description);
        if (isOk) {
            newCategory.setText("");
            newDescription.setText("");
            SuccessfullCategoryRegister.setText("Category added successfully");
            SuccessfullCategoryRegister.setVisible(true);
        }else{
            SuccessfullCategoryRegister.setText("Category already or incorrect");
            SuccessfullCategoryRegister.setVisible(true);
        }
    }
}
