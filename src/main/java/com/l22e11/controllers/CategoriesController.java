package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import com.l22e11.helper.FieldValidation;
import com.l22e11.helper.AccountWrapper;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;

public class CategoriesController implements Initializable {

    @FXML
    private Button addCategory, removeCategory;
    @FXML
	private TextField categoryName;
    @FXML
    private TextArea categoryDescription;
    @FXML
	private AnchorPane categoryNameBack, categoryDescriptionBack;

    @FXML
	private Label categoryNameError, categoryDescriptionError;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // FieldValidation.authenticationBoxes = new TextInputControl[]{categoryName, categoryDescription};
        // FieldValidation.authenticationBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack};
        // FieldValidation.authenticationErrorMessages = new Label[]{categoryNameError, categoryDescriptionError};
    }

    private boolean validateCategory() {
        String name = categoryName.getText();
        String description = categoryDescription.getText();
        boolean isOk = AccountWrapper.registerCategory(name, description);
        
        // setInputBoxColor(authenticationBoxes[CATEGORY_NAME_IDX], authenticationBoxesBack[CATEGORY_NAME_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        // setInputBoxColor(authenticationBoxes[CATEGORY_DESCRIPTION_IDX], authenticationBoxesBack[CATEGORY_DESCRIPTION_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        
        // categoryNameError.setText(NAME_ERROR);
        // categoryNameError.setVisible(!isOk);
        // categoryDescriptionError.setText(DESCRIPTION_ERROR);
        // categoryDescriptionError.setVisible(!isOk);
        
        return isOk;
    }
    
    @FXML //TODO 
    private void onAddCategory(ActionEvent event) {
        boolean isOk = validateCategory();
        // if (isOk) // SHOW IT WAS SUCCESFULL
    }
    
    @FXML //TODO
    private void onRemoveCategory(ActionEvent event) {

        // List<Category> categories = AccountWrapper.getUserCategories();
        // Category victimCategory = 

        // if (categories.contains(victimCategory)) AccountWrapper.removeCategory(); CATEGORY REMOVED CORRECTLY
        // else CATEGORY WAS ALREADY REMOVED
    }

    @FXML //TODO
    private void onListCategories(ActionEvent event) {

        // List<Category> categories = AccountWrapper.getUserCategories();
        // DISPLAY EACH CATEGORY
    }
}
