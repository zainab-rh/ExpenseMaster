package com.l22e11.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.l22e11.helper.FieldValidation;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Colors;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import model.Category; // Import the Category class

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
<<<<<<< HEAD
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
=======
        FieldValidation.inputBoxes = new TextInputControl[]{categoryName, categoryDescription};
        FieldValidation.inputBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack};
        FieldValidation.inputErrorMessages = new Label[]{categoryNameError, categoryDescriptionError};
    }

     private boolean validateCategory() {
            boolean isOk = FieldValidation.validateCategoryName() && FieldValidation.validateCategoryDescription();
            return isOk;
  
>>>>>>> 4fd0fe1fe9fbd8c8fd53c9caac0af1e178ce2f15
    }
    @FXML //TODO 
    private void onAddCategory(ActionEvent event) {
        boolean isOk = validateCategory();
        if (isOk) {
            categoryName.clear();
            categoryDescription.clear();
            categoryNameError.setVisible(false);
            categoryDescriptionError.setVisible(false);
        }

    }
    @FXML //TODO
 
    private void onRemoveCategory(ActionEvent event) {
        String categoryNameText = categoryName.getText();
        List<Category> categories = AccountWrapper.getUserCategories();
        Category victimCategory = null;
        for (Category category : categories) {
            if (category.getName().equals(categoryNameText)) {
                victimCategory = category;
                break;
            }
        }
        if (victimCategory!= null) {
            if (AccountWrapper.removeCategory(victimCategory)) {
                categoryName.clear();
                categoryDescription.clear();
                categoryNameError.setVisible(false);
                categoryDescriptionError.setVisible(false);
            } else {
                categoryNameError.setText("Error removing category");
                categoryNameError.setVisible(true);
            }
        } else {
            categoryNameError.setText("Category not found");
            categoryNameError.setVisible(true);
        }
    }
    private void onListCategories(ActionEvent event) {

        List<Category> categories = AccountWrapper.getUserCategories();
        // DISPLAY EACH CATEGORY
        for (Category category : categories) {
            System.out.println(category.getName() + " - " + category.getDescription());
        }
    }
    }


