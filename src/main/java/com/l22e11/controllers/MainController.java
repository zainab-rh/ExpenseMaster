package com.l22e11.controllers;

import javafx.scene.input.MouseEvent;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.List;
import model.Category;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Utils;
import com.l22e11.helper.Colors;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ChoiceBox;
import model.User;

public class MainController implements Initializable {

    @FXML
    private AnchorPane sideBar;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label fullName;
    @FXML
    private Button logOutSubmit; // addCategory

    /*@FXML
	private TextField categoryName, chargeName, chargeDescription, chargeCost, chargeUnits, chargeCategory;
    @FXML
    private TextArea categoryDescription;
    @FXML
	private AnchorPane categoryNameBack, categoryDescriptionBack, chargeNameBack, chargeDescriptionBack, chargeCostBack, chargeUnitsBack, chargeCategoryBack;

    @FXML
	private Label categoryNameError, categoryDescriptionError, chargeNameError, chargeDescriptionError, chargeCostError, chargeUnitsError, chargeCategoryError;

    private final int CATEGORY_NAME_IDX = 0, CATEGORY_DESCRIPTION_IDX = 1, CHARGE_NAME_IDX = 2, CHARGE_DESCRIPTION_IDX = 3, CHARGE_COST_IDX = 4, CHARGE_UNITS_IDX = 5, CHARGE_CATEGORY_IDX = 6;
    private final int MAX_LENGTH_DESCRIPTION = 500;
    private TextInputControl inputBoxes[];
    private AnchorPane inputBoxesBack[];
    private Label inputErrorMessages[];


    private final Pattern NAMES_PATTERN = Pattern.compile("^([A-ZÑ]{1}[a-zñ]{1,}[\\s]{0,1})*$");
    private final Pattern EXTRA_SPACES_PATTERN = Pattern.compile("(^\\s|\\s\\s|\\s$)");
    private final Pattern DIGITS_AND_SYMBOLS_PATTERN = Pattern.compile("[^A-ZÑa-zñ\\s]");
    private final String NAME_ERROR = "Invalid name of a category";
    private final String DESCRIPTION_ERROR = "Description should be less than 500 charcaters";
    private final String EMPTY_ERROR = "Field empty";
    private final String DIGITS_AND_SYMBOLS_ERROR = "Remove any symbols or digits";
    private final String CAPITALIZATION_ERROR = "Incorrect capitalization"; 
    */
	@Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        User user = AccountWrapper.getAuthenticatedUser();
        Platform.runLater(() -> {
            profilePic.setImage(user.getImage());
            fullName.setText(user.getName() + " " + user.getSurname());
        });

        /*inputBoxes = new TextInputControl[]{categoryName, categoryDescription, chargeName, chargeDescription, chargeCost, chargeUnits, chargeCategory};
        inputBoxesBack = new AnchorPane[]{categoryNameBack, categoryDescriptionBack, chargeNameBack, chargeDescriptionBack, chargeCostBack, chargeUnitsBack, chargeCategoryBack};
        inputErrorMessages = new Label[]{categoryNameError, categoryDescriptionError, chargeNameError, chargeDescriptionError, chargeCostError, chargeUnitsError, chargeCategoryError};

        for (int idx = 0; idx < 2; ++idx) {
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
            // inputBoxes[i].setOnKeyPressed((event) -> {
            //     if (event.getCode() == KeyCode.ENTER) {
            //         KeyEvent tabKeyEvent = new KeyEvent(
            //             KeyEvent.KEY_PRESSED,
            //             KeyCode.TAB.getChar(),
            //             KeyCode.TAB.getName(),
            //             KeyCode.TAB,
            //             false, false, false, false
            //         );
            //         inputBoxes[i].fireEvent(tabKeyEvent);
            //     }
            // });
        }*/
        
    }
	/*
    //TODO
    private boolean validateField(int i) {
        switch (i) {
            case CATEGORY_NAME_IDX:            return validateName(i);
            case CATEGORY_DESCRIPTION_IDX:            return validateDescription(i);
            case CHARGE_NAME_IDX:         return validateName(i);
            // case CHARGE_DESCRIPTION_IDX:      return (i);
            // case CHARGE_COST_IDX:     return (i);
            // case CHARGE_UNITS_IDX:        return (i);
            // case CHARGE_CATEGORY_IDX:         return (i);
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

    
    private boolean validateName(int idx) {
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
    
    private boolean validateDescription(int idx) {
        if (checkIfEmpty(idx)) return false;

        String textToCheck = inputBoxes[idx].getText();

        boolean result = true;

        if (textToCheck.length() > MAX_LENGTH_DESCRIPTION){
            inputErrorMessages[idx].setText(DESCRIPTION_ERROR);
            result = false;
        }

        inputErrorMessages[idx].setVisible(!result);

        return result;
    }
    
    
    private boolean validateCategory() {
        String name = categoryName.getText();
        String description = categoryDescription.getText();
        boolean isOk = AccountWrapper.registerCategory(name, description);
        
        setInputBoxColor(inputBoxes[CATEGORY_NAME_IDX], inputBoxesBack[CATEGORY_NAME_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        setInputBoxColor(inputBoxes[CATEGORY_DESCRIPTION_IDX], inputBoxesBack[CATEGORY_DESCRIPTION_IDX], true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        
        categoryNameError.setText(NAME_ERROR);
        categoryNameError.setVisible(!isOk);
        categoryDescriptionError.setText(DESCRIPTION_ERROR);
        categoryDescriptionError.setVisible(!isOk);
        
        return isOk;
    }
    
    @FXML //TODO 
    private void onAddCategory(ActionEvent event) {

        boolean nameOk = validateField(CATEGORY_NAME_IDX);
        boolean descriptionOk = validateField(CATEGORY_DESCRIPTION_IDX);
        
        if (!nameOk || !descriptionOk) return;
        
        boolean isOk = validateCategory();
        // if (isOk) // SHOW IT WAS SUCCESFULL
    }
    
    @FXML //TODO
    private void onRemoveCategory(ActionEvent event) {

        List<Category> categories = AccountWrapper.getUserCategories();
        // Category victimCategory = 

        // if (categories.contains(victimCategory)) AccountWrapper.removeCategory(); CATEGORY REMOVED CORRECTLY
        // else CATEGORY WAS ALREADY REMOVED
    }

    @FXML //TODO
    private void onListCategories(ActionEvent event) {

        List<Category> categories = AccountWrapper.getUserCategories();
        // DISPLAY EACH CATEGORY
    }

    @FXML //TODO
    private void onListCategories(ActionEvent event) {

        List<Category> categories = AccountWrapper.getUserCategories();
        // DISPLAY EACH CATEGORY
    }




    private void setInputBoxColor(Node inputBox, Node inputBoxBack, boolean active, String color) {
        inputBox.setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
        inputBoxBack.setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
    }*/



    @FXML
    private void onLogOut(MouseEvent event) {
        boolean isOk = AccountWrapper.logOutUser();
        if (isOk) App.showLandingStage();
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
