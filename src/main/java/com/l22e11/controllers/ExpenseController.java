package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.FieldValidation;
import com.l22e11.helper.SideTab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Charge;

public class ExpenseController implements Initializable {

	public static Charge currentCharge;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

	@FXML
	private void onDiscardChanges(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard this expense?");
		if (alert.showAndWait().isPresent()) {
			currentCharge = null;
			MainController.setSideTab(SideTab.NONE);
		}
	}

	@FXML
	private void onSaveChanges(ActionEvent event) {
		// if (FieldValidation.checkExpenseFields()) {
		// 	AccountWrapper.registerCharge(

		// 	);

		// 	// TODO: Update listview
		// 	currentCharge = null;
		// 	MainController.setSideTab(SideTab.NONE);
		// }
		// User user = AccountWrapper.getAuthenticatedUser();
		// if (FieldValidation.checkRegisterFields()) {
		// 	AccountWrapper.updateUser(user,
		// 		FieldValidation.authenticationBoxes[FieldValidation.REGISTER_NAME_IDX].getText(),
		// 		FieldValidation.authenticationBoxes[FieldValidation.REGISTER_SURNAME_IDX].getText(),
		// 		FieldValidation.authenticationBoxes[FieldValidation.REGISTER_EMAIL_IDX].getText(),
		// 		FieldValidation.authenticationBoxes[FieldValidation.REGISTER_PASS_IDX].getText(),
		// 		FieldValidation.authenticationProfileImage.getImage()
		// 	);

		// 	Alert alert = new Alert(AlertType.CONFIRMATION);
		// 	alert.setTitle("Confirm Save");
		// 	alert.setHeaderText("Save Changes");
		// 	alert.setContentText("Are you sure you want to save your changes?");
		// 	if (alert.showAndWait().isPresent()) {
		// 		resetFields();
		// 		MainController.reloadSideBar();
		// 	}
		// }
	}
}
