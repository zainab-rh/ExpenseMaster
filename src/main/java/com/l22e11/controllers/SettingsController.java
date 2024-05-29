package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.LoginFieldValidation;
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
		LoginFieldValidation.authenticationBoxes = new TextInputControl[]{null, null, updateName, updateSurname, updateNickname, updateEmail, updatePass, updatePassConfirm, null};
        LoginFieldValidation.authenticationBoxesBack = new AnchorPane[]{null, null, updateNameBack, updateSurnameBack, updateNicknameBack, updateEmailBack, updatePassBack, updatePassConfirmBack, null};
        LoginFieldValidation.authenticationErrorMessages = new Label[]{null, null, updateNameError, updateSurnameError, updateNicknameError, updateEmailError, updatePassError, updatePassConfirmError, updateNicknameError};
		LoginFieldValidation.authenticationProfileImage = profilePic;

		resetFields();

        for (int idx = LoginFieldValidation.REGISTER_NAME_IDX; idx <= LoginFieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			LoginFieldValidation.setFocusListener(idx); // When obtaining or losing focus validate field anc change colour
            LoginFieldValidation.setTabSimulator(idx); // When pressing enter, traverse to next focusable elements
        }

		profilePicPane.setOnMouseClicked((event) -> {
			browseProfilePicture();
		});

		// Disable nickname input box
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
		LoginFieldValidation.populateFieldsWithUser(user, profilePicPaneCroppable);

        for (int idx = LoginFieldValidation.REGISTER_NAME_IDX; idx <= LoginFieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			LoginFieldValidation.validateField(idx);
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

	@FXML //TODO: Image is optional
	private void onSaveChanges(ActionEvent event) {
		User user = AccountWrapper.getAuthenticatedUser();
		if (LoginFieldValidation.checkRegisterFields()) {
			AccountWrapper.updateUser(user,
				LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_NAME_IDX].getText(),
				LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_SURNAME_IDX].getText(),
				LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_EMAIL_IDX].getText(),
				LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_PASS_IDX].getText(),
				LoginFieldValidation.authenticationProfileImage.getImage()
			);

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Save");
			alert.setHeaderText("Save Changes");
			alert.setContentText("Are you sure you want to save your changes?");
			if (alert.showAndWait().isPresent()) {
				resetFields();
				MainController.reloadTabBar();
			}
		}
	}
}
