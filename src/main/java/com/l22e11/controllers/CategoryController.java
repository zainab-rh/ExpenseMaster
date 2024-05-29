package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import com.l22e11.helper.CategoryFieldValidation;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CategoryFieldValidation.categoryBoxes = new TextInputControl[]{categoryName, categoryDescription};
        CategoryFieldValidation.categoryBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack};
        CategoryFieldValidation.categoryErrorMessages = new Label[]{categoryNameError, categoryDescriptionError};

		CategoryFieldValidation.setFocusListener(CategoryFieldValidation.CATEGORY_NAME_IDX);
		CategoryFieldValidation.setTabSimulator(CategoryFieldValidation.CATEGORY_NAME_IDX);
		CategoryFieldValidation.setFocusListener(CategoryFieldValidation.CATEGORY_DESCRIPTION_IDX);
    }

    @FXML //TODO: Set color
    private void onSaveChanges(ActionEvent event) {
		if (!CategoryFieldValidation.checkCategoryFields()) return;

        if (CategoryFieldValidation.validateAndRegisterCategory()) {
            GlobalState.reloadCategories();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Category Saved");
            alert.setHeaderText("Category Saved");
            alert.setContentText("Category saved correctly");
            if (alert.showAndWait().isPresent()) {
                GlobalState.currentCategory = null;
                MainController.setSideTab(SideTab.NONE);
            }
        }
    }

	@FXML
    private void onDiscardChanges(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard this category?");
		if (alert.showAndWait().isPresent()) {
			GlobalState.currentCategory = null;
			MainController.setSideTab(SideTab.NONE);
		}
    }
}
