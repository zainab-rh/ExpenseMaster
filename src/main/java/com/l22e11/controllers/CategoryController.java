package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import com.l22e11.helper.CategoryFieldValidation;
import com.l22e11.helper.GlobalState;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;


import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
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
	private AnchorPane categoryNameBack, categoryDescriptionBack, colourPickerBack;
	@FXML
	private ColorPicker colourPicker;
    @FXML
	private Label categoryNameError, categoryDescriptionError, constructiveLabel, destructiveLabel, categoryColourError, colourPickerArrow;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        CategoryFieldValidation.categoryBoxes = new TextInputControl[]{categoryName, categoryDescription, null};
        CategoryFieldValidation.categoryBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack, colourPickerBack};
        CategoryFieldValidation.categoryErrorMessages = new Label[]{categoryNameError, categoryDescriptionError, categoryColourError};
		CategoryFieldValidation.colourPicker = colourPicker;

		CategoryFieldValidation.setTabSimulator(CategoryFieldValidation.CATEGORY_NAME_IDX);
		CategoryFieldValidation.setFocusListener(CategoryFieldValidation.CATEGORY_NAME_IDX);
		CategoryFieldValidation.setFocusListener(CategoryFieldValidation.CATEGORY_DESCRIPTION_IDX);
		CategoryFieldValidation.setColourPickerTabSimulator();

        CategoryFieldValidation.populateFields();
        CategoryFieldValidation.setChangeListeners();

        if (GlobalState.currentCategory == null) {
			constructiveLabel.setText("Save Category");
			destructiveLabel.setText("Discard Category");
		} else {
			constructiveLabel.setText("Save Changes");
			destructiveLabel.setText("Discard Changes");
		}

		colourPickerArrow.setOnMouseClicked((event) -> {
			colourPicker.requestFocus();
			colourPicker.show();
		});

		colourPicker.valueProperty().addListener((event) -> {
			CategoryFieldValidation.setColourPickerBoxColor(colourPicker.getValue());
		});
    }

    @FXML
    private void onSaveChanges(ActionEvent event) {
		if (!CategoryFieldValidation.checkCategoryFields()) return;

        if (CategoryFieldValidation.registerOrUpdateCategory()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Category Saved");
            alert.setHeaderText("Category Saved");
            alert.setContentText("Category saved correctly");
            if (alert.showAndWait().isPresent()) {
				GlobalState.changesInCurrentCategory = false;
                GlobalState.currentCategory = null;
                MainController.closeSideTab();
            }
        }
    }

	@FXML
    private void onDiscardChanges(ActionEvent event) {
		if (requestDiscardChanges()) MainController.closeSideTab();
    }

    public static boolean requestDiscardChanges() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard your changes?");
		if (GlobalState.changesInCurrentCategory == false || alert.showAndWait().get() == ButtonType.OK) {
			GlobalState.currentCategory = null;
            GlobalState.changesInCurrentCategory = false;
            return true;
		}
        return false;
    }
}
