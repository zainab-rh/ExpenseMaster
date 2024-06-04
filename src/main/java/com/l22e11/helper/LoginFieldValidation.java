package com.l22e11.helper;

import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class LoginFieldValidation {

	public static final int LOGIN_USER_IDX = 0, LOGIN_PASS_IDX = 1, REGISTER_NAME_IDX = 2, REGISTER_SURNAME_IDX = 3, REGISTER_NICKNAME_IDX = 4, REGISTER_EMAIL_IDX = 5, REGISTER_PASS_IDX = 6, REGISTER_PASS_CONFIRM_IDX = 7, REGISTER_PROFILE_IMAGE_IDX = 8;
    
	public static final Pattern NICK_PATTERN = Pattern.compile("^[A-Za-z0-9]{4,}$");
    public static final Pattern NAMES_PATTERN = Pattern.compile("^([A-ZÑ]{1}[a-zñ]{1,}[\\s]{0,1})*$");
    public static final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("(^\\s|\\s\\s|\\s$)");
    public static final Pattern DIGITS_AND_SYMBOLS_PATTERN = Pattern.compile("[^A-ZÑa-zñ\\s]");
    public static final Pattern CAPITALIZATION_PATTERN = Pattern.compile("(^[a-zñ]|\\s[a-zñ])");
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^\\S+@\\S+\\.\\S+$");
    public static final String NICK_ERROR = "Nickname should only contain letters and digits, no symbols, spaces, accents or 'ñ' and should be 4 characters or longer";
    public static final String PASS_ERROR = "Password should contain more than 5 characters";
    public static final String PASS_CONFIRM_ERROR = "Passwords don't match";
    public static final String LOGIN_MISMATCH = "Nickname and password do not match, check the nickname and retype the password";
    public static final String EXTRA_SPACES_ERROR = "Please remove extra spaces";
    public static final String EMPTY_ERROR = "Field empty";
    public static final String DIGITS_AND_SYMBOLS_ERROR = "Remove any symbols or digits";
    public static final String CAPITALIZATION_ERROR = "Incorrect capitalization"; 
    public static final String EMAIL_ERROR = "Email adress does not look correct";
    public static final String NO_IMAGE_ERROR = "Please select an image";
    public static final String NICK_PICKED = "This nickname is unavailable";

	public static Label authenticationErrorMessages[];
    public static TextInputControl authenticationBoxes[];
    public static AnchorPane authenticationBoxesBack[];
	public static ImageView authenticationProfileImage;

	public static void setInputBoxColor(int i, boolean active, String color) {
		authenticationBoxes[i].setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
		authenticationBoxesBack[i].setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
    }

	public static void setFocusListener(int i) {
		authenticationBoxes[i].focusedProperty().addListener((obs, oldV, newV) ->  {
			if (newV.booleanValue()) { setInputBoxColor(i, true, Colors.BLUE_ACCENT); }
			else {
				if (authenticationBoxes[i].getText().length() == 0) { setInputBoxColor(i, false, ""); }
				else { validateField(i); }
			}
		});
	}

	public static void setTabSimulator(int i) {
		authenticationBoxes[i].setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				KeyEvent tabKeyEvent = new KeyEvent(
					KeyEvent.KEY_PRESSED,
					KeyCode.TAB.getChar(),
					KeyCode.TAB.getName(),
					KeyCode.TAB,
					false, false, false, false
				);
				authenticationBoxes[i].fireEvent(tabKeyEvent);
			}
		});
	}

	public static boolean validateField(int i) {
        switch (i) {
            case LOGIN_USER_IDX:             return validateLoginUser();
            case LOGIN_PASS_IDX:             return validateLoginPass();
            case REGISTER_NAME_IDX:          return validateRegisterName();
            case REGISTER_SURNAME_IDX:       return validateRegisterSurname();
            case REGISTER_NICKNAME_IDX:      return validateRegisterNickname();
            case REGISTER_EMAIL_IDX:         return validateRegisterEmail();
            case REGISTER_PASS_IDX:          return validateRegisterPass();
            case REGISTER_PASS_CONFIRM_IDX:  return validateRegisterPassConfirm();
			case REGISTER_PROFILE_IMAGE_IDX: return validateImage();
        }
		return false;
    }

    public static boolean checkIfEmpty(int i) {
        boolean isEmpty = authenticationBoxes[i].getText().length() == 0;

        authenticationErrorMessages[i].setText(EMPTY_ERROR);
        authenticationErrorMessages[i].setVisible(true);

        setInputBoxColor(i, true, Colors.RED_ACCENT);

        return isEmpty;
    }

    public static boolean validateLoginUser() {
        if (checkIfEmpty(LOGIN_USER_IDX)) return false;
        
        String textToCheck = authenticationBoxes[LOGIN_USER_IDX].getText();

        boolean result = NICK_PATTERN.matcher(textToCheck).matches();
        if (!result) authenticationErrorMessages[LOGIN_USER_IDX].setText(NICK_ERROR);

        authenticationErrorMessages[LOGIN_USER_IDX].setVisible(!result);
        if (!result) setInputBoxColor(LOGIN_USER_IDX, true, Colors.RED_ACCENT);
        else setInputBoxColor(LOGIN_USER_IDX, false, "");
        return result;
    }
    
    public static boolean validateLoginPass() {
        if (checkIfEmpty(LOGIN_PASS_IDX)) return false;

        authenticationErrorMessages[LOGIN_PASS_IDX].setVisible(false);
        setInputBoxColor(LOGIN_PASS_IDX, false/*true*/, Colors.GREEN_ACCENT);
        return true;
    }
    
    public static boolean validateRegisterName() {
        if (checkIfEmpty(REGISTER_NAME_IDX)) return false;

        String textToCheck = authenticationBoxes[REGISTER_NAME_IDX].getText();
        String inputWithoutDiacritics = Utils.removeAccents(textToCheck);

        boolean result = true;
        
        // Check for digits and symbols
        if (DIGITS_AND_SYMBOLS_PATTERN.matcher(inputWithoutDiacritics).find()) {
            authenticationErrorMessages[REGISTER_NAME_IDX].setText(DIGITS_AND_SYMBOLS_ERROR);
            result = false;
        }
        
        authenticationErrorMessages[REGISTER_NAME_IDX].setVisible(!result);
        setInputBoxColor(REGISTER_NAME_IDX, true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        if (result) {
            textToCheck = Utils.removeExtraWhiteSpaces(textToCheck);
            textToCheck = Utils.capitalize(textToCheck);
            authenticationBoxes[REGISTER_NAME_IDX].setText(textToCheck);
        }

        return result;
    }
    
    public static boolean validateRegisterSurname() {
        if (checkIfEmpty(REGISTER_SURNAME_IDX)) return false;

        String textToCheck = authenticationBoxes[REGISTER_SURNAME_IDX].getText();
        String inputWithoutDiacritics = Utils.removeAccents(textToCheck);

        boolean result = true;

        // Check for digits and symbols
        if (DIGITS_AND_SYMBOLS_PATTERN.matcher(inputWithoutDiacritics).find()) {
            authenticationErrorMessages[REGISTER_SURNAME_IDX].setText(DIGITS_AND_SYMBOLS_ERROR);
            result = false;
        }
        
        authenticationErrorMessages[REGISTER_SURNAME_IDX].setVisible(!result);
        setInputBoxColor(REGISTER_SURNAME_IDX, true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        if (result) {
            textToCheck = Utils.removeExtraWhiteSpaces(textToCheck);
            textToCheck = Utils.capitalize(textToCheck);
            authenticationBoxes[REGISTER_SURNAME_IDX].setText(textToCheck);
        }

        return result;
    }
    
    public static boolean validateRegisterNickname() {
        if (checkIfEmpty(REGISTER_NICKNAME_IDX)) return false;

        String textToCheck = authenticationBoxes[REGISTER_NICKNAME_IDX].getText();
        
        boolean result = NICK_PATTERN.matcher(textToCheck).matches();
        if (!result) authenticationErrorMessages[REGISTER_NICKNAME_IDX].setText(NICK_ERROR);

        authenticationErrorMessages[REGISTER_NICKNAME_IDX].setVisible(!result);
        setInputBoxColor(REGISTER_NICKNAME_IDX, true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    public static boolean validateRegisterEmail() {
        if (checkIfEmpty(REGISTER_EMAIL_IDX)) return false;

        String textToCheck = authenticationBoxes[REGISTER_EMAIL_IDX].getText();

        boolean result = EMAIL_PATTERN.matcher(textToCheck).matches();
        if (!result) authenticationErrorMessages[REGISTER_EMAIL_IDX].setText(EMAIL_ERROR);
        
        authenticationErrorMessages[REGISTER_EMAIL_IDX].setVisible(!result);
        setInputBoxColor(REGISTER_EMAIL_IDX, true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    public static boolean validateRegisterPass() {
        if (checkIfEmpty(REGISTER_PASS_IDX)) return false;

        String textToCheck = authenticationBoxes[REGISTER_PASS_IDX].getText();

        boolean result = textToCheck.length() >= 6;
        if (!result) authenticationErrorMessages[REGISTER_PASS_IDX].setText(PASS_ERROR);

        authenticationErrorMessages[REGISTER_PASS_IDX].setVisible(!result);
        setInputBoxColor(REGISTER_PASS_IDX, true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    public static boolean validateRegisterPassConfirm() {
        if (checkIfEmpty(REGISTER_PASS_CONFIRM_IDX)) return false;

        String textToCheck = authenticationBoxes[REGISTER_PASS_CONFIRM_IDX].getText();

        boolean result = textToCheck.equals(authenticationBoxes[REGISTER_PASS_IDX].getText());
        if (!result) authenticationErrorMessages[REGISTER_PASS_CONFIRM_IDX].setText(PASS_CONFIRM_ERROR);

        authenticationErrorMessages[REGISTER_PASS_CONFIRM_IDX].setVisible(!result);
        setInputBoxColor(REGISTER_PASS_CONFIRM_IDX, true, (result ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        return result;
    }
    
    public static boolean validateImage() {
		// boolean result = authenticationProfileImage.getImage() != null;
        boolean result = true;
        
        if (!result) authenticationErrorMessages[REGISTER_PROFILE_IMAGE_IDX].setText(NO_IMAGE_ERROR);
        authenticationErrorMessages[REGISTER_PROFILE_IMAGE_IDX].setVisible(!result);

        return result;
    }

	public static boolean checkLoginFields() {
		boolean allGood = true;
		allGood &= validateLoginUser();
        allGood &= validateLoginPass();
		return allGood;
	}

	public static boolean checkRegisterFields() {
		boolean allGood = true;
		allGood &= validateRegisterName();
        allGood &= validateRegisterSurname();
        allGood &= validateRegisterNickname();
        allGood &= validateRegisterEmail();
        allGood &= validateRegisterPass();
        allGood &= validateRegisterPassConfirm();
        allGood &= validateImage();
		return allGood;
	}

	public static boolean validateLogin() {
        String nick = authenticationBoxes[LOGIN_USER_IDX].getText();
        String pass = authenticationBoxes[LOGIN_PASS_IDX].getText();
        boolean isOk = AccountWrapper.loginUser(nick, pass);

        setInputBoxColor(LOGIN_USER_IDX, true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        setInputBoxColor(LOGIN_PASS_IDX, true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

        authenticationErrorMessages[LOGIN_PASS_IDX].setText(LOGIN_MISMATCH);
        authenticationErrorMessages[LOGIN_PASS_IDX].setVisible(!isOk);
        authenticationBoxes[LOGIN_PASS_IDX].setText("");

        return isOk;
    }

	public static boolean validateRegister() {
        String name = authenticationBoxes[REGISTER_NAME_IDX].getText();
        String surname = authenticationBoxes[REGISTER_SURNAME_IDX].getText();
        String email = authenticationBoxes[REGISTER_EMAIL_IDX].getText();
        String nick = authenticationBoxes[REGISTER_NICKNAME_IDX].getText();
        String pass = authenticationBoxes[REGISTER_PASS_IDX].getText();
        Image profilePic = authenticationProfileImage.getImage();
        LocalDate dateNow = LocalDate.now();

        int result = AccountWrapper.registerUser(name, surname, email, nick, pass, profilePic, dateNow);

        if (result != 1) {
            authenticationErrorMessages[REGISTER_NICKNAME_IDX].setText(NICK_PICKED);
            authenticationErrorMessages[REGISTER_NICKNAME_IDX].setVisible(true);
            setInputBoxColor(REGISTER_NICKNAME_IDX, true, Colors.RED_ACCENT);

            return false;
        }

        authenticationBoxes[LOGIN_USER_IDX].setText(nick);
        authenticationBoxes[LOGIN_PASS_IDX].setText(pass);

        return true;
    }

    public static void updateUser() {
        AccountWrapper.updateUser(GlobalState.user,
            authenticationBoxes[REGISTER_NAME_IDX].getText(),
            authenticationBoxes[REGISTER_SURNAME_IDX].getText(),
            authenticationBoxes[REGISTER_EMAIL_IDX].getText(),
            authenticationBoxes[REGISTER_PASS_IDX].getText(),
            authenticationProfileImage.getImage()
        );
    }

    public static void setChangeListeners() {
		authenticationBoxes[REGISTER_NAME_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.settingsTabModified = true;
		});
		authenticationBoxes[REGISTER_SURNAME_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.settingsTabModified = true;
		});
		authenticationBoxes[REGISTER_EMAIL_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.settingsTabModified = true;
		});
		authenticationBoxes[REGISTER_PASS_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.settingsTabModified = true;
		});
		authenticationProfileImage.imageProperty().addListener((obs, oldV, newV) -> {
			GlobalState.settingsTabModified = true;
		});
    }

	public static void populateFields(Region profilePicPaneCroppable) {
		authenticationBoxes[REGISTER_NAME_IDX].setText(GlobalState.user.getName());
		authenticationBoxes[REGISTER_SURNAME_IDX].setText(GlobalState.user.getSurname());
		authenticationBoxes[REGISTER_NICKNAME_IDX].setText(GlobalState.user.getNickName());
		authenticationBoxes[REGISTER_EMAIL_IDX].setText(GlobalState.user.getEmail());
		authenticationBoxes[REGISTER_PASS_IDX].setText(GlobalState.user.getPassword());
		authenticationBoxes[REGISTER_PASS_CONFIRM_IDX].setText(GlobalState.user.getPassword());
		
		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			authenticationProfileImage.setImage(Utils.cropImage(GlobalState.user.getImage(), profilePicPaneCroppable, Integer.MAX_VALUE));
		}), 50, TimeUnit.MILLISECONDS);
	}
}
