package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.l22e11.App;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.LoginFieldValidation;
import com.l22e11.helper.Utils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SettingsController implements Initializable {

	@FXML
    private ImageView profilePic;
	@FXML
	private Pane profilePicPane, profilePicPaneCroppable;
	@FXML
	private TextField updateName, updateSurname, updateNickname, updateEmail, updatePassReveal, updatePassConfirmReveal;
	@FXML
	private PasswordField updatePass, updatePassConfirm;
	@FXML
	private AnchorPane updateNameBack, updateSurnameBack, updateNicknameBack, updateEmailBack, updatePassBack, updatePassConfirmBack;
	@FXML
	private Label updateNameError, updateSurnameError, updateNicknameError, updateEmailError, updatePassError, updatePassConfirmError, nickNameLabel, passEye, passConfirmEye;

	private static Pane staticProfilePicPaneCroppable;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		staticProfilePicPaneCroppable = profilePicPaneCroppable;
		LoginFieldValidation.authenticationBoxes = new TextInputControl[]{null, null, updateName, updateSurname, updateNickname, updateEmail, updatePass, updatePassConfirm, null};
        LoginFieldValidation.authenticationBoxesBack = new AnchorPane[]{null, null, updateNameBack, updateSurnameBack, updateNicknameBack, updateEmailBack, updatePassBack, updatePassConfirmBack, null};
        LoginFieldValidation.authenticationErrorMessages = new Label[]{null, null, updateNameError, updateSurnameError, updateNicknameError, updateEmailError, updatePassError, updatePassConfirmError, updateNicknameError};
		LoginFieldValidation.authenticationProfileImage = profilePic;

        for (int idx = LoginFieldValidation.REGISTER_NAME_IDX; idx <= LoginFieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			LoginFieldValidation.setFocusListener(idx);
            LoginFieldValidation.setTabSimulator(idx);
        }

		LoginFieldValidation.populateFields(staticProfilePicPaneCroppable);
		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			LoginFieldValidation.setChangeListeners();
		}), 100, TimeUnit.MILLISECONDS);

		// Disable nickname input box
		nickNameLabel.setManaged(false);
		updateNicknameBack.setManaged(false);
		updateNicknameError.setManaged(false);

		setUpPasswordReveal();
    }

	private void setUpPasswordReveal() {
		passEye.setOnMousePressed((event) -> {
			updatePassReveal.setText(updatePass.getText());
			updatePass.setVisible(false);
			passEye.setText("");
		});
		passEye.setOnMouseReleased((event) -> {
			updatePass.setVisible(true);
			passEye.setText("");
		});
		passConfirmEye.setOnMousePressed((event) -> {
			updatePassConfirmReveal.setText(updatePassConfirm.getText());
			updatePassConfirm.setVisible(false);
			passConfirmEye.setText("");
		});
		passConfirmEye.setOnMouseReleased((event) -> {
			updatePassConfirm.setVisible(true);
			passConfirmEye.setText("");
		});
	}

	@FXML
	private void onBrowseProfileImage(ActionEvent event) {
		Image croppedImage = Utils.loadNewPictureInto(profilePicPaneCroppable, Integer.MAX_VALUE);
        if (croppedImage == null) return;
		profilePic.setImage(croppedImage);
	}

	@FXML
	private void onResetProfileImage(ActionEvent event) {
		profilePic.setImage(App.defaultImage);
	}

	@FXML
	private void onSaveChanges(ActionEvent event) {
		if (LoginFieldValidation.checkRegisterFields()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Save");
			alert.setHeaderText("Save Changes");
			alert.setContentText("Are you sure you want to save your changes?");
			if (alert.showAndWait().isPresent()) {
				LoginFieldValidation.updateUser();
				MainController.reloadTabBar();
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
		alert.setContentText("Are you sure you want to discard the changes to your profile?");
		alert.setResizable(true);
		alert.setWidth(600);
		alert.setHeight(400);
		if (GlobalState.settingsTabModified == false || alert.showAndWait().get() == ButtonType.OK) {
			LoginFieldValidation.populateFields(staticProfilePicPaneCroppable);
			LoginFieldValidation.setChangeListeners();
			Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
            	GlobalState.settingsTabModified = false;
			}), 100, TimeUnit.MILLISECONDS);
            return true;
		}
        return false;
    }
}
