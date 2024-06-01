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
import javafx.scene.control.ButtonType;
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
	private Label categoryNameError, categoryDescriptionError, constructiveLabel, destructiveLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CategoryFieldValidation.categoryBoxes = new TextInputControl[]{categoryName, categoryDescription};
        CategoryFieldValidation.categoryBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack};
        CategoryFieldValidation.categoryErrorMessages = new Label[]{categoryNameError, categoryDescriptionError};

		CategoryFieldValidation.setTabSimulator(CategoryFieldValidation.CATEGORY_NAME_IDX);
		CategoryFieldValidation.setFocusListener(CategoryFieldValidation.CATEGORY_NAME_IDX);
		CategoryFieldValidation.setFocusListener(CategoryFieldValidation.CATEGORY_DESCRIPTION_IDX);

        CategoryFieldValidation.populateFields();
        CategoryFieldValidation.setChangeListeners();

        if (GlobalState.currentCategory == null) {
			constructiveLabel.setText("Save Category");
			destructiveLabel.setText("Discard Category");
		} else {
			constructiveLabel.setText("Save Changes");
			destructiveLabel.setText("Discard Changes");
		}
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
                // TODO: Add category
                GlobalState.currentCategory = null;
                MainController.setSideTab(SideTab.NONE);
            }
        }
    }

	@FXML
    private void onDiscardChanges(ActionEvent event) {
		requestDiscardChanges();
    }

    public static boolean requestDiscardChanges() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard your changes?");
		if (GlobalState.sideTabModified == false || alert.showAndWait().get() == ButtonType.OK) {
			GlobalState.currentCategory = null;
            GlobalState.sideTabModified = false;
			MainController.setSideTab(SideTab.NONE);
            return true;
		}
        return false;
    }
}
