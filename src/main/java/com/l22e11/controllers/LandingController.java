package com.l22e11.controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Colors;
import com.l22e11.helper.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.skin.TextInputControlSkin.Direction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

public class LandingController implements Initializable {

    @FXML
    private Pane rootPane;
    @FXML
    private Tab loginTab, registerTab;
    @FXML
    private TabPane authenticationPane;
	@FXML
	private AnchorPane loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack;
	@FXML
	private TextField loginUser, registerName, registerSurname, registerNickname, registerEmail;
	@FXML
	private PasswordField loginPass, registerPass, registerPassConfirm;
	@FXML
    private Button loginSubmit, registerBrowseProfilePic, registerSubmit;
    @FXML
    private ImageView registerProfileView;
    @FXML
	private Label loginError, registerNameError, registerSurnameError, registerNicknameError, registerEmailError, registerPassError, registerPassConfirmError, registerBrowseProfilePicError;

    private final int LOGIN_USER_IDX = 0, LOGIN_PASS_IDX = 1, REGISTER_NAME_IDX = 2, REGISTER_SURNAME_IDX = 3, REGISTER_NICKNAME_IDX = 4, REGISTER_EMAIL_IDX = 5, REGISTER_PASS_IDX = 6, REGISTER_PASS_CONFIRM_IDX = 7;
    private TextInputControl inputBoxes[];
    private AnchorPane inputBoxesBack[];

    private final Pattern NICK_PATTERN = Pattern.compile("^[A-Za-z0-9]{4,}$");
    private final Pattern NAMES_PATTERN = Pattern.compile("^([A-ZÑ]{1}[a-zñ]{1,}[\\s]{0,1})*$");
    private final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("(^\\s|\\s\\s|\\s$)");
    private final Pattern DIGITS_AND_SYMBOLS_PATTERN = Pattern.compile("[^A-ZÑa-zñ\\s]");
    private final Pattern CAPITALIZATION_PATTERN = Pattern.compile("(^[a-zñ]|\\s[a-zñ])");
    private final Pattern EMAIL_PATTERN = Pattern.compile("^\\S+@\\S+\\.\\S+$");
    private final String NICK_ERROR = "Nickname should only contain letters and digits, no symbols, spaces, accents or 'ñ' and should be 4 characters or longer";
    private final String PASS_ERROR = "Password should contain more than 8 characters";
    private final String PASS_CONFIRM_ERROR = "Passwords don't match";
    private final String LOGIN_MISMATCH = "Nickname and password do not match, check the nickname and retype the password";
    private final String EXTRA_SPACES_ERROR = "Please remove extra spaces";
    private final String NAMES_ERROR = "Wrong format";
    private final String DIGITS_AND_SYMBOLS_ERROR = "Remove any symbols or digits";
    private final String CAPITALIZATION_ERROR = "Incorrect capitalization"; 
    private final String EMAIL_ERROR = "Email adress does not look correct";
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        inputBoxes = new TextInputControl[]{loginUser, loginPass, registerName, registerSurname, registerNickname, registerEmail, registerPass, registerPassConfirm};
        inputBoxesBack = new AnchorPane[]{loginUserBack, loginPassBack, registerNameBack, registerSurnameBack, registerNicknameBack, registerEmailBack, registerPassBack, registerPassConfirmBack};

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

    private boolean validateLoginUser(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        boolean result = NICK_PATTERN.matcher(textToCheck).matches();
        
        if (!result) loginError.setText(NICK_ERROR);
        loginError.setVisible(!result);
        
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateLoginPass(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        boolean result = textToCheck.length() >= 8;

        if (!result) loginError.setText(PASS_ERROR);
        loginError.setVisible(!result);

        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterName(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        String inputWithoutDiacritics = Utils.removeAccents(textToCheck);

        boolean allGood = true;

        // Check for extra spaces
        if (allGood && EXTRA_SPACES_PATTERN.matcher(inputWithoutDiacritics).find()) {
            registerNameError.setText(EXTRA_SPACES_ERROR);
            allGood = false;
        }
        
        // Check for correct capitalization
        if (allGood && CAPITALIZATION_PATTERN.matcher(inputWithoutDiacritics).find()) {
            registerNameError.setText(CAPITALIZATION_ERROR);
            allGood = false;
        }
        
        // Check for digits and symbols
        if (allGood && DIGITS_AND_SYMBOLS_PATTERN.matcher(inputWithoutDiacritics).find()) {
            registerNameError.setText(DIGITS_AND_SYMBOLS_ERROR);
            allGood = false;
        }
        
        // Check for names starting with uppercase followed by lowercase possibly followed by a whitespace
        if (allGood && !NAMES_PATTERN.matcher(inputWithoutDiacritics).matches()) {
            registerNameError.setText(CAPITALIZATION_ERROR);
            allGood = false;
        }
        
        registerNameError.setVisible(!allGood);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        return allGood;
    }
    
    private boolean validateRegisterSurname(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        String inputWithoutDiacritics = Utils.removeAccents(textToCheck);

        boolean allGood = true;

        // Check for extra spaces
        if (allGood && EXTRA_SPACES_PATTERN.matcher(inputWithoutDiacritics).find()) {
            registerSurnameError.setText(EXTRA_SPACES_ERROR);
            allGood = false;
        }
        
        // Check for correct capitalization
        if (allGood && CAPITALIZATION_PATTERN.matcher(inputWithoutDiacritics).find()) {
            registerSurnameError.setText(CAPITALIZATION_ERROR);
            allGood = false;
        }
        
        // Check for digits and symbols
        if (allGood && DIGITS_AND_SYMBOLS_PATTERN.matcher(inputWithoutDiacritics).find()) {
            registerSurnameError.setText(DIGITS_AND_SYMBOLS_ERROR);
            allGood = false;
        }
        
        // Check for names starting with uppercase followed by lowercase possibly followed by a whitespace
        if (allGood && !NAMES_PATTERN.matcher(inputWithoutDiacritics).matches()) {
            registerSurnameError.setText(CAPITALIZATION_ERROR);
            allGood = false;
        }
        
        registerSurnameError.setVisible(!allGood);
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        return allGood;
    }
    
    private boolean validateRegisterNickname(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        boolean result = NICK_PATTERN.matcher(textToCheck).matches();
        
        if (!result) registerNicknameError.setText(NICK_ERROR);
        registerNicknameError.setVisible(!result);
        
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterEmail(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        boolean result = EMAIL_PATTERN.matcher(textToCheck).matches();
        
        if (!result) registerNicknameError.setText(EMAIL_ERROR);
        registerEmailError.setVisible(!result);
        
        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterPass(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        boolean result = textToCheck.length() >= 8;

        if (!result) registerPass.setText(PASS_ERROR);
        registerPass.setVisible(!result);

        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateRegisterPassConfirm(int idx) {
        String textToCheck = inputBoxes[idx].getText();
        boolean result = textToCheck == registerPass.getText();

        if (!result) registerPassConfirm.setText(PASS_CONFIRM_ERROR);
        registerPassConfirm.setVisible(!result);

        setInputBoxColor(inputBoxes[idx], inputBoxesBack[idx], true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    private boolean validateImage() {
        return false; //TODO:
    }

    private boolean validateLogin() {
        String nick = loginUser.getText();
        String pass = loginPass.getText();
        boolean isOk = AccountWrapper.loginUser(nick, pass);

        setInputBoxColor(inputBoxes[LOGIN_USER_IDX], inputBoxesBack[LOGIN_USER_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        setInputBoxColor(inputBoxes[LOGIN_PASS_IDX], inputBoxesBack[LOGIN_PASS_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        loginError.setText(LOGIN_MISMATCH);
        loginError.setVisible(!isOk);
        loginPass.setText("");

        return isOk;
    }

    @FXML
    private void onSubmitLogin(ActionEvent event) {
        boolean nickOk = validateField(LOGIN_USER_IDX);
        boolean passOk = validateField(LOGIN_PASS_IDX);

        if (!nickOk) return;
        if (!passOk) return;

        boolean isOk = validateLogin();
        if (isOk) { App.showMainStage(); }
    }

    // private boolean validateRegister() { // TODO:
    //     String name = registerName.getText();
    //     String surname = registerSurname.getText();
    //     String nick = registerNickname.getText();
    //     String email = registerEmail.getText();
    //     String pass = registerPass.getText();
    //     String passConfirmation = registerPassConfirm.getText();
    //     Image profilePic = registerProfileView.getImage();
    //     LocalDate dateNow = LocalDate.now();
    // }

    @FXML
    private void onSubmitRegister(ActionEvent event) {
        boolean nameOk = validateField(REGISTER_NAME_IDX);
        boolean surnameOk = validateField(REGISTER_SURNAME_IDX);
        boolean nickOk = validateField(REGISTER_NICKNAME_IDX);
        boolean emailOk = validateField(REGISTER_EMAIL_IDX);
        boolean passOk = validateField(REGISTER_PASS_IDX);
        boolean passConfirmationOk = validateField(REGISTER_PASS_CONFIRM_IDX);
        boolean profilePicOk = validateImage();
        //TODO:
    }

    
    @FXML
    private void onBrowseProfilePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(App.getMainStage());
        if (selectedFile != null) { //TODO:
            Image image = new Image(selectedFile.toURI().toString());
            registerProfileView.setImage(image);
        }
    }
}
