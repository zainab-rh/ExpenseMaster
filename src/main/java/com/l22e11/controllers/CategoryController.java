package com.l22e11.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.l22e11.helper.CategoryFieldValidation;
import com.l22e11.helper.SideTab;
import com.l22e11.helper.MainTab;
import com.l22e11.helper.AccountWrapper;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;


import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import model.Category; // Import the Category class

public class CategoryController implements Initializable {

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

	public static Category currentCategory;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CategoryFieldValidation.categoryBoxes = new TextInputControl[]{categoryName, categoryDescription};
        CategoryFieldValidation.categoryBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack};
        CategoryFieldValidation.categoryErrorMessages = new Label[]{categoryNameError, categoryDescriptionError};

		for (int idx = CategoryFieldValidation.CATEGORY_NAME_IDX; idx <= CategoryFieldValidation.CATEGORY_DESCRIPTION_IDX; ++idx) {
			CategoryFieldValidation.setFocusListener(idx);
			CategoryFieldValidation.setTabSimulator(idx);
		}
    }

    @FXML //TODO 
    private void onSaveChanges(ActionEvent event) {
		if (!CategoryFieldValidation.checkCategoryFields()) return;

        if (CategoryFieldValidation.validateCategory()) {
            AccountWrapper.registerCategory(categoryName.getText(), categoryDescription.getText());
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Category saver");
		    alert.setHeaderText("");
            alert.setContentText("Category saved correctly");
            if (alert.showAndWait().isPresent()) {
                currentCategory = null;
                MainController.setSideTab(SideTab.NONE);
                // BAD BUT CORRECT IMPLEMENTATION TO REFRESH
                MainController.setMainTab(MainTab.DASHBOARD);
                MainController.setMainTab(MainTab.CATEGORIES);
            }
        }
    }

	@FXML //TODO 
    private void onDiscardChanges(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard this category?");
		if (alert.showAndWait().isPresent()) {
			currentCategory = null;
			MainController.setSideTab(SideTab.NONE);
		}
    }
}
