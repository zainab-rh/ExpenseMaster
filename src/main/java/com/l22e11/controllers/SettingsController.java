package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.FieldValidation;
import com.l22e11.helper.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.User;

public class SettingsController implements Initializable {

	@FXML
    private ImageView profilePic;
	@FXML
	private Pane profilePicPane, profilePicPaneCroppable;
	@FXML
	private TextField updateName, updateSurname, updateNickname, updateEmail;
	@FXML
	private PasswordField updatePass, updatePassConfirm;
	@FXML
	private AnchorPane updateNameBack, updateSurnameBack, updateNicknameBack, updateEmailBack, updatePassBack, updatePassConfirmBack;
	@FXML
	private Label updateNameError, updateSurnameError, updateNicknameError, updateEmailError, updatePassError, updatePassConfirmError, nickNameLabel;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {		
		FieldValidation.inputBoxes = new TextInputControl[]{null, null, updateName, updateSurname, updateNickname, updateEmail, updatePass, updatePassConfirm, null};
        FieldValidation.inputBoxesBack = new AnchorPane[]{null, null, updateNameBack, updateSurnameBack, updateNicknameBack, updateEmailBack, updatePassBack, updatePassConfirmBack, null};
        FieldValidation.inputErrorMessages = new Label[]{null, null, updateNameError, updateSurnameError, updateNicknameError, updateEmailError, updatePassError, updatePassConfirmError, updateNicknameError};
		FieldValidation.profileImage = profilePic;

		resetFields();

        for (int idx = FieldValidation.REGISTER_NAME_IDX; idx <= FieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			FieldValidation.setFocusListener(idx); // When obtaining or losing focus validate field anc change colour
            FieldValidation.setTabSimulator(idx); // When pressing enter, traverse to next focusable elements
        }

		profilePicPane.setOnMouseClicked((event) -> {
			browseProfilePicture();
		});

		nickNameLabel.setManaged(false);
		updateNicknameBack.setManaged(false);
		updateNicknameError.setManaged(false);
    }

    private void browseProfilePicture() {
		Image croppedImage = Utils.loadNewProfilePictureInto(profilePicPaneCroppable);

        if (croppedImage == null) return;

		profilePic.setImage(croppedImage);
    }

	private void resetFields() {
		User user = AccountWrapper.getAuthenticatedUser();
		FieldValidation.populateFieldsWithUser(user, profilePicPaneCroppable);

        for (int idx = FieldValidation.REGISTER_NAME_IDX; idx <= FieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			FieldValidation.validateField(idx);
        }
	}

	@FXML
	private void onDiscardChanges(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard your changes?");
		if (alert.showAndWait().isPresent()) resetFields();
	}

	@FXML
	private void onSaveChanges(ActionEvent event) {
		User user = AccountWrapper.getAuthenticatedUser();
		if (FieldValidation.checkRegisterFields()) {
			AccountWrapper.updateUser(user,
				FieldValidation.inputBoxes[FieldValidation.REGISTER_NAME_IDX].getText(),
				FieldValidation.inputBoxes[FieldValidation.REGISTER_SURNAME_IDX].getText(),
				FieldValidation.inputBoxes[FieldValidation.REGISTER_EMAIL_IDX].getText(),
				FieldValidation.inputBoxes[FieldValidation.REGISTER_PASS_IDX].getText(),
				FieldValidation.profileImage.getImage()
			);

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Save");
			alert.setHeaderText("Save Changes");
			alert.setContentText("Are you sure you want to save your changes?");
			if (alert.showAndWait().isPresent()) {
				resetFields();
				MainController.reloadSideBar();
			}
		}
	}
}
