package com.l22e11.controllers;

import java.net.URL;
import javafx.scene.input.MouseEvent;
import java.util.Optional;
import java.util.ResourceBundle;
import com.l22e11.App;
import com.l22e11.helper.LoginFieldValidation;
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
	private AnchorPane loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack;
	@FXML
	private TextField loginUser, registerName, registerSurname, registerNickname, registerEmail, registerPassReveal, registerPassConfirmReveal;
	@FXML
	private PasswordField loginPass, registerPass, registerPassConfirm;
	@FXML
    private Button loginSubmit, registerBrowseProfilePic, resetBrowseProfilePic, registerSubmit;
    @FXML
    private ImageView registerProfileView;
    @FXML
	private Label loginNameError, loginPassError, registerNameError, registerSurnameError, registerNicknameError, registerEmailError, registerPassError, registerPassConfirmError, registerBrowseProfilePicError, forgotPassword, goToLogin, goToRegister, passEye, passConfirmEye;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        LoginFieldValidation.authenticationBoxes = new TextInputControl[]{loginUser, loginPass, registerName, registerSurname, registerNickname, registerEmail, registerPass, registerPassConfirm, null};
        LoginFieldValidation.authenticationBoxesBack = new AnchorPane[]{loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack, null};
        LoginFieldValidation.authenticationErrorMessages = new Label[]{loginNameError, loginPassError, registerNameError, registerSurnameError, registerNicknameError, registerEmailError, registerPassError, registerPassConfirmError, registerBrowseProfilePicError};
		LoginFieldValidation.authenticationProfileImage = registerProfileView;

        for (int idx = LoginFieldValidation.LOGIN_USER_IDX; idx <= LoginFieldValidation.REGISTER_PASS_CONFIRM_IDX; ++idx) {
			LoginFieldValidation.setFocusListener(idx); // When obtaining or losing focus validate field anc change colour
            LoginFieldValidation.setTabSimulator(idx); // When pressing enter, traverse to next focusable element
        }

		// During login, if user presses Enter attempt login
        loginPass.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
				// BUGGED
                onSubmitLogin(new ActionEvent());
            }
        });

		// Forgot password or nickname, ask for email as long as email is not in a valid format
        forgotPassword.setOnMouseClicked((event) -> {
            String email = "";
            boolean hasCanceled = false;

            while (!LoginFieldValidation.EMAIL_PATTERN.matcher(email).matches() && !hasCanceled) {
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

		resetProfileImage();
		setUpPasswordReveal();
    }

    @FXML
    private void onSubmitLogin(ActionEvent event) {
        if (!LoginFieldValidation.checkLoginFields()) return;

        boolean isOk = LoginFieldValidation.validateLogin();
        if (isOk) App.showMainStage();
    }

    @FXML
    private void onSubmitRegister(ActionEvent event) {
        if (!LoginFieldValidation.checkRegisterFields()) return;

        boolean isOk = LoginFieldValidation.validateRegister();
        if (isOk) {
            authenticationPane.getSelectionModel().selectFirst();

            for (int i = LoginFieldValidation.REGISTER_NAME_IDX; i <= LoginFieldValidation.REGISTER_PASS_CONFIRM_IDX; ++i) {
                LoginFieldValidation.authenticationBoxes[i].setText("");
                LoginFieldValidation.setInputBoxColor(i, false, "");
            }
            resetProfileImage();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Register successful");
            alert.setHeaderText("Register successful");
            alert.setContentText("You registered correctly, you can change your personal details at any time by logging into the app.");
            alert.showAndWait();
        }
    }

	private void resetProfileImage() {
		registerProfileView.setImage(App.defaultImage);
	}

	private void setUpPasswordReveal() {
		passEye.setOnMousePressed((event) -> {
			registerPassReveal.setText(registerPass.getText());
			registerPass.setVisible(false);
			passEye.setText("");
		});
		passEye.setOnMouseReleased((event) -> {
			registerPass.setVisible(true);
			passEye.setText("");
		});
		passConfirmEye.setOnMousePressed((event) -> {
			registerPassConfirmReveal.setText(registerPassConfirm.getText());
			registerPassConfirm.setVisible(false);
			passConfirmEye.setText("");
		});
		passConfirmEye.setOnMouseReleased((event) -> {
			registerPassConfirm.setVisible(true);
			passConfirmEye.setText("");
		});
	}

    @FXML
    private void onBrowseProfileImage(ActionEvent event) {
		Image croppedImage = Utils.loadNewPictureInto(registerProfileViewPaneCroppable, Integer.MAX_VALUE);

        if (croppedImage == null) {
			registerBrowseProfilePicError.setText("This image appears to have 0 width and height");
			registerBrowseProfilePicError.setVisible(true);
			return;
		}

		registerProfileView.setImage(croppedImage);
		registerBrowseProfilePicError.setVisible(false);
    }

	@FXML
	private void onResetProfileImage(ActionEvent event) {
		resetProfileImage();
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
