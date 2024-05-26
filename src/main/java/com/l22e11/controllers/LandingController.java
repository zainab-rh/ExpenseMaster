package com.l22e11.controllers;

import java.net.URL;
import javafx.scene.input.MouseEvent;
import java.util.Optional;
import java.util.ResourceBundle;
import com.l22e11.App;
import com.l22e11.helper.FieldValidation;
import com.l22e11.helper.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class LandingController implements Initializable {

    @FXML
    private Pane rootPane, registerProfileViewPane, registerProfileViewPaneCroppable;
    @FXML
    private Tab loginTab, registerTab;
    @FXML
    private TabPane authenticationPane;
	@FXML
	private AnchorPane loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack, profilePicPicker;
	@FXML
	private TextField loginUser, registerName, registerSurname, registerNickname, registerEmail;
	@FXML
	private PasswordField loginPass, registerPass, registerPassConfirm;
	@FXML
    private Button loginSubmit, registerBrowseProfilePic, registerSubmit;
    @FXML
    private ImageView registerProfileView;
    @FXML
	private Label loginNameError, loginPassError, registerNameError, registerSurnameError, registerNicknameError, registerEmailError, registerPassError, registerPassConfirmError, registerBrowseProfilePicError, registerProfilePicBrowseLabel, forgotPassword, goToLogin, goToRegister;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        FieldValidation.authenticationBoxes = new TextInputControl[]{loginUser, loginPass, registerName, registerSurname, registerNickname, registerEmail, registerPass, registerPassConfirm, null};
        FieldValidation.authenticationBoxesBack = new AnchorPane[]{loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack, null};
        FieldValidation.authenticationErrorMessages = new Label[]{loginNameError, loginPassError, registerNameError, registerSurnameError, registerNicknameError, registerEmailError, registerPassError, registerPassConfirmError, registerBrowseProfilePicError};
		FieldValidation.authenticationProfileImage = registerProfileView;

        for (int idx = FieldValidation.LOGIN_USER_IDX; idx <= FieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			FieldValidation.setFocusListener(idx); // When obtaining or losing focus validate field anc change colour
            FieldValidation.setTabSimulator(idx); // When pressing enter, traverse to next focusable element
        }

		// Hide Image View when user has not yet selected image
        registerProfileViewPane.setManaged(false);
        registerProfileViewPane.setVisible(false);

		// During login, if user presses Enter attempt login
        loginPass.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSubmitLogin(null);
            }
        });

		// Forgot password or nickname, ask for email as long as email is not in a valid format
        forgotPassword.setOnMouseClicked((event) -> {
            String email = "";
            boolean hasCanceled = false;

            while (!FieldValidation.EMAIL_PATTERN.matcher(email).matches() && !hasCanceled) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Reset Password or Nickname");
                dialog.setHeaderText("Enter your email to send a password reset request, you will be reminded of your nickname in case you forgot it");
                dialog.setContentText("Email:");

                Optional<String> opt = dialog.showAndWait();
                email = opt.get();
                hasCanceled = !opt.isPresent();
            }
        });

		// User presses label prompting to change authentication method
        goToLogin.setOnMouseClicked((event) -> {
            authenticationPane.getSelectionModel().selectFirst();
        });
        goToRegister.setOnMouseClicked((event) -> {
            authenticationPane.getSelectionModel().selectLast();
        });
    }

    @FXML
    private void onSubmitLogin(ActionEvent event) {
        if (!FieldValidation.checkLoginFields()) return;

        boolean isOk = FieldValidation.validateLogin();
        if (isOk) App.showMainStage();
    }

    @FXML
    private void onSubmitRegister(ActionEvent event) {
        if (!FieldValidation.checkRegisterFields()) return;

        boolean isOk = FieldValidation.validateRegister();
        if (isOk) {
            authenticationPane.getSelectionModel().selectFirst();

            for (int i = FieldValidation.REGISTER_NAME_IDX; i <= FieldValidation.REGISTER_PASS_CONFIRM_IDX; ++i) {
                FieldValidation.authenticationBoxes[i].setText("");
                FieldValidation.setInputBoxColor(i, false, "");
            }
            registerProfileView.setImage(null);
            registerProfileViewPane.setManaged(false);
            registerProfileViewPane.setVisible(false);
            registerProfilePicBrowseLabel.setText("Browse profile picture");

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Register successful");
            alert.setHeaderText("Register successful");
            alert.setContentText("You registered correctly, you can change your personal details at any time by logging into the app.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onBrowseProfilePicture(ActionEvent event) {
		Image croppedImage = Utils.loadNewProfilePictureInto(registerProfileViewPaneCroppable);

        if (croppedImage == null) {
			registerBrowseProfilePicError.setText("This image appears to have 0 width and height");
			registerBrowseProfilePicError.setVisible(true);
			return;
		}

		registerProfileView.setImage(croppedImage);
		registerProfileViewPane.setManaged(true);
		registerProfileViewPane.setVisible(true);
		registerProfilePicBrowseLabel.setText("Change profile picture");
		registerBrowseProfilePicError.setVisible(false);
    }
    
    @FXML
    private void onAppMinimize(MouseEvent event) {
        App.minimize();
    }
    
    @FXML
    private void onAppClose(MouseEvent event) {
        App.close();
    }
}
