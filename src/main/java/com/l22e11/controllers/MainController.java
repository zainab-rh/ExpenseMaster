package com.l22e11.controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class MainController implements Initializable{

    @FXML
    private TextField newCategory, newDescription;
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
