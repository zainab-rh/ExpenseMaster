package com.l22e11.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import javafx.scene.input.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Colors;
import com.l22e11.helper.Utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
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
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.Acount;

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

    private final int LOGIN_USER_IDX = 0, LOGIN_PASS_IDX = 1, REGISTER_NAME_IDX = 2, REGISTER_SURNAME_IDX = 3, REGISTER_NICKNAME_IDX = 4, REGISTER_EMAIL_IDX = 5, REGISTER_PASS_IDX = 6, REGISTER_PASS_CONFIRM_IDX = 7;
    private Label inputErrorMessages[];
    private TextInputControl inputBoxes[];
    private AnchorPane inputBoxesBack[];

    private final Pattern NICK_PATTERN = Pattern.compile("^[A-Za-z0-9]{4,}$");
    private final Pattern NAMES_PATTERN = Pattern.compile("^([A-ZÑ]{1}[a-zñ]{1,}[\\s]{0,1})*$");
    private final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("(^\\s|\\s\\s|\\s$)");
    private final Pattern DIGITS_AND_SYMBOLS_PATTERN = Pattern.compile("[^A-ZÑa-zñ\\s]");
    private final Pattern CAPITALIZATION_PATTERN = Pattern.compile("(^[a-zñ]|\\s[a-zñ])");
    private final Pattern EMAIL_PATTERN = Pattern.compile("^\\S+@\\S+\\.\\S+$");
    private final String NICK_ERROR = "Nickname should only contain letters and digits, no symbols, spaces, accents or 'ñ' and should be 4 characters or longer";
    private final String PASS_ERROR = "Password should contain more than 5 characters";
    private final String PASS_CONFIRM_ERROR = "Passwords don't match";
    private final String LOGIN_MISMATCH = "Nickname and password do not match, check the nickname and retype the password";
    private final String EXTRA_SPACES_ERROR = "Please remove extra spaces";
    private final String EMPTY_ERROR = "Field empty";
    private final String DIGITS_AND_SYMBOLS_ERROR = "Remove any symbols or digits";
    private final String CAPITALIZATION_ERROR = "Incorrect capitalization"; 
    private final String EMAIL_ERROR = "Email adress does not look correct";
    private final String NO_IMAGE_ERROR = "Please select an image";
    private final String NICK_PICKED = "This nickname is unavailable";
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        inputBoxes = new TextInputControl[]{loginUser, loginPass, registerName, registerSurname, registerNickname, registerEmail, registerPass, registerPassConfirm};
        inputBoxesBack = new AnchorPane[]{loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack};
        inputErrorMessages = new Label[]{loginNameError, loginPassError, registerNameError, registerSurnameError, registerNicknameError, registerEmailError, registerPassError, registerPassConfirmError};

        for (int idx = 0; idx < inputBoxes.length; ++idx) {
            final int i = idx;
            // When obtaining or losing focus validate field anc change colour
            inputBoxes[i].focusedProperty().addListener((obs, oldV, newV) ->  {
                if (newV.booleanValue()) { setInputBoxColor(inputBoxes[i], inputBoxesBack[i], true, Colors.BLUE_ACCENT); }
                else {
                    if (inputBoxes[i].getText().length() == 0) { setInputBoxColor(inputBoxes[i], inputBoxesBack[i], false, ""); }
                    else { validateField(i); }
                }
            });
            // When pressing enter, traverse to next focusable element
            inputBoxes[i].setOnKeyPressed((event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    KeyEvent tabKeyEvent = new KeyEvent(
                        KeyEvent.KEY_PRESSED,
                        KeyCode.TAB.getChar(),
                        KeyCode.TAB.getName(),
                        KeyCode.TAB,
                        false, false, false, false
                    );
                    inputBoxes[i].fireEvent(tabKeyEvent);
                }
            });
        }

        registerProfileViewPane.setManaged(false);
        registerProfileViewPane.setVisible(false);
        loginPass.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                onSubmitLogin(null);
            }
        });

        forgotPassword.setOnMouseClicked((event) -> {
            String email = "";
            boolean hasCanceled = false;

            while (!EMAIL_PATTERN.matcher(email).matches() && !hasCanceled) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Reset Password");
                dialog.setHeaderText("Enter your email to send a password reset request");
                dialog.setContentText("Email:");

                Optional<String> opt = dialog.showAndWait();
                email = opt.get();
                hasCanceled = !opt.isPresent();
            }
        });

        goToLogin.setOnMouseClicked((event) -> {
            authenticationPane.getSelectionModel().selectFirst();
        });
        goToRegister.setOnMouseClicked((event) -> {
            authenticationPane.getSelectionModel().selectLast();
        });
    }

    private void setInputBoxColor(Node inputBox, Node inputBoxBack, boolean active, String color) {
        inputBox.setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
        inputBoxBack.setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
    }

    private boolean validateField(int i) {
        switch (i) {
            case LOGIN_USER_IDX:            return validateLoginUser(i);
            case LOGIN_PASS_IDX:            return validateLoginPass(i);
            case REGISTER_NAME_IDX:         return validateRegisterName(i);
            case REGISTER_SURNAME_IDX:      return validateRegisterSurname(i);
            case REGISTER_NICKNAME_IDX:     return validateRegisterNickname(i);
            case REGISTER_EMAIL_IDX:        return validateRegisterEmail(i);
            case REGISTER_PASS_IDX:         return validateRegisterPass(i);
            case REGISTER_PASS_CONFIRM_IDX: return validateRegisterPassConfirm(i);
            default:                        return false;
        }
    }

    private boolean checkIfEmpty(int idx) {
        boolean isEmpty = inputBoxes[idx].getText().length() == 0;

        inputErrorMessages[idx].setText(EMPTY_ERROR);
        inputErrorMessages[idx].setVisible(true);

        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, Colors.RED_ACCENT);

        return isEmpty;
    }

    private boolean validateLoginUser(int idx) {
        if (checkIfEmpty(idx)) return false;
        
        String textToCheck = inputBoxes[idx].getText();

        boolean result = NICK_PATTERN.matcher(textToCheck).matches();
        if (!result) inputErrorMessages[idx].setText(NICK_ERROR);

        inputErrorMessages[idx].setVisible(!result);
        if (!result) setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, Colors.RED_ACCENT);
        else setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], false, "");
        return result;
    }
    
    private boolean validateLoginPass(int idx) {
        if (checkIfEmpty(idx)) return false;
        
        // String textToCheck = inputBoxes[idx].getText();

        boolean result = true;
        // boolean result = textToCheck.length() >= 8;
        // if (!result) inputErrorMessages[idx].setText(PASS_ERROR);

        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], false/*true*/, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterName(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();
        String inputWithoutDiacritics = Utils.removeAccents(textToCheck);

        boolean result = true;
        
        // Check for digits and symbols
        if (DIGITS_AND_SYMBOLS_PATTERN.matcher(inputWithoutDiacritics).find()) {
            inputErrorMessages[idx].setText(DIGITS_AND_SYMBOLS_ERROR);
            result = false;
        }
        
        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        if (result) {
            textToCheck = Utils.removeExtraWhiteSpaces(textToCheck);
            textToCheck = Utils.capitalize(textToCheck);
            inputBoxes[idx].setText(textToCheck);
        }

        return result;
    }
    
    private boolean validateRegisterSurname(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();
        String inputWithoutDiacritics = Utils.removeAccents(textToCheck);

        boolean result = true;

        // Check for digits and symbols
        if (DIGITS_AND_SYMBOLS_PATTERN.matcher(inputWithoutDiacritics).find()) {
            inputErrorMessages[idx].setText(DIGITS_AND_SYMBOLS_ERROR);
            result = false;
        }
        
        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        if (result) {
            textToCheck = Utils.removeExtraWhiteSpaces(textToCheck);
            textToCheck = Utils.capitalize(textToCheck);
            inputBoxes[idx].setText(textToCheck);
        }

        return result;
    }
    
    private boolean validateRegisterNickname(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();
        
        boolean result = NICK_PATTERN.matcher(textToCheck).matches();
        if (!result) inputErrorMessages[idx].setText(NICK_ERROR);

        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterEmail(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();

        boolean result = EMAIL_PATTERN.matcher(textToCheck).matches();
        if (!result) inputErrorMessages[idx].setText(EMAIL_ERROR);
        
        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterPass(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();

        boolean result = textToCheck.length() >= 6;
        if (!result) inputErrorMessages[idx].setText(PASS_ERROR);

        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterPassConfirm(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();

        boolean result = textToCheck.equals(registerPass.getText());
        if (!result) inputErrorMessages[idx].setText(PASS_CONFIRM_ERROR);

        inputErrorMessages[idx].setVisible(!result);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateImage() {
        boolean result = registerProfileView.getImage() != null;
        
        if (!result) registerBrowseProfilePicError.setText(NO_IMAGE_ERROR);
        registerBrowseProfilePicError.setVisible(!result);

        return result;
    }

    private boolean validateLogin() {
        String nick = loginUser.getText();
        String pass = loginPass.getText();
        boolean isOk = AccountWrapper.loginUser(nick, pass);

        setInputBoxColor(inputBoxes[LOGIN_USER_IDX], inputBoxesBack[LOGIN_USER_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        setInputBoxColor(inputBoxes[LOGIN_PASS_IDX], inputBoxesBack[LOGIN_PASS_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        loginPassError.setText(LOGIN_MISMATCH);
        loginPassError.setVisible(!isOk);
        loginPass.setText("");

        return isOk;
    }

    @FXML
    private void onSubmitLogin(ActionEvent event) {
        boolean nickOk = validateField(LOGIN_USER_IDX);
        boolean passOk = validateField(LOGIN_PASS_IDX);
        
        if (!nickOk || !passOk) return;

        boolean isOk = validateLogin();
        if (isOk) App.showMainStage();
    }

    private boolean validateRegister() {
        String name = registerName.getText();
        String surname = registerSurname.getText();
        String nick = registerNickname.getText();
        String email = registerEmail.getText();
        String pass = registerPass.getText();
        Image profilePic = registerProfileView.getImage();
        LocalDate dateNow = LocalDate.now();

        int result = AccountWrapper.registerUser(name, surname, email, nick, pass, profilePic, dateNow);
        System.out.println(result);

        if (result != 1) {
            registerNicknameError.setText(NICK_PICKED);
            registerNicknameError.setVisible(true);
            setInputBoxColor(inputBoxes[REGISTER_NICKNAME_IDX], inputBoxesBack[REGISTER_NICKNAME_IDX], true, Colors.RED_ACCENT);

            return false;
        }

        loginUser.setText(nick);
        loginPass.setText(pass);

        return true;
    }

    @FXML
    private void onSubmitRegister(ActionEvent event) {
        boolean nameOk = validateField(REGISTER_NAME_IDX);
        boolean surnameOk = validateField(REGISTER_SURNAME_IDX);
        boolean nickOk = validateField(REGISTER_NICKNAME_IDX);
        boolean emailOk = validateField(REGISTER_EMAIL_IDX);
        boolean passOk = validateField(REGISTER_PASS_IDX);
        boolean passConfirmationOk = validateField(REGISTER_PASS_CONFIRM_IDX);
        boolean profilePicOk = validateImage();
        
        if (!nameOk || !surnameOk || !nickOk || !emailOk || !passOk || !passConfirmationOk || !profilePicOk) return;

        boolean isOk = validateRegister();
        if (isOk) {
            authenticationPane.getSelectionModel().selectFirst();

            for (int i = REGISTER_NAME_IDX; i <= REGISTER_PASS_CONFIRM_IDX; ++i) {
                inputBoxes[i].setText("");
                setInputBoxColor(inputBoxes[i], inputBoxesBack[i], false, "");
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
    private void onBrowseProfilePicture(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(App.getMainStage());
        if (selectedFile != null) {
            File file = new File(selectedFile.toURI());
            BufferedImage bufferedImage = ImageIO.read(file);
            
            Image croppedImage = Utils.cropImage(SwingFXUtils.toFXImage(bufferedImage, null), registerProfileViewPaneCroppable);

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
