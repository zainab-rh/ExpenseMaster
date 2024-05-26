package com.l22e11.controllers;

import javafx.scene.input.MouseEvent;
import java.util.Map;
import static java.util.Map.entry;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Utils;
import com.l22e11.helper.MainTab;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import model.User;

public class MainController implements Initializable {

    @FXML
    private AnchorPane sideBar;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label fullName;
	@FXML
	private Pane profilePicPaneCroppable;
	@FXML
	private HBox logOutArea, mainTab;
	@FXML
	private VBox userArea, tabOptions;

	private MainTab currentTab = MainTab.NONE;
	final MainTab TABS[] = {MainTab.DASHBOARD, MainTab.EXPENSES, MainTab.CATEGORIES};
	final Map<MainTab, Integer> TABS_MAP = Map.ofEntries(
		entry(MainTab.NONE, 0),
		entry(TABS[0], 0),
		entry(TABS[1], 1),
		entry(TABS[2], 2)
	);
	ObservableList<Node> tabList;
    // @FXML
    // private Button addCategory, removeCategory;
    // @FXML
	// private TextField categoryName, chargeName, chargeDescription, chargeCost, chargeUnits, chargeCategory;
    // @FXML
    // private TextArea categoryDescription;
    // @FXML
	// private AnchorPane categoryNameBack, categoryDescriptionBack, chargeNameBack, chargeDescriptionBack, chargeCostBack, chargeUnitsBack, chargeCategoryBack;

    // @FXML
	// private Label categoryNameError, categoryDescriptionError, chargeNameError, chargeDescriptionError, chargeCostError, chargeUnitsError, chargeCategoryError;

    /* 

    private final int CATEGORY_NAME_IDX = 0, CATEGORY_DESCRIPTION_IDX = 1, CHARGE_NAME_IDX = 2, CHARGE_DESCRIPTION_IDX = 3, CHARGE_COST_IDX = 4, CHARGE_UNITS_IDX = 5, CHARGE_CATEGORY_IDX = 6;
    private final int MAX_LENGTH_DESCRIPTION = 500;
    */
	@Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		// Display name and profile picture in sidebar
        User user = AccountWrapper.getAuthenticatedUser();

		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			profilePic.setImage(Utils.cropImage(user.getImage(), profilePicPaneCroppable));
            fullName.setText(user.getName() + " " + user.getSurname());
		}), 50, TimeUnit.MILLISECONDS);

		// Logout button
		logOutArea.setOnMouseClicked((event) -> {
			boolean isOk = AccountWrapper.logOutUser();
        	if (isOk) App.showLandingStage();
		});

		userArea.setOnMouseClicked((event) -> {setMainTab(MainTab.SETTINGS);});

		tabList = tabOptions.getChildren();
		for (int idx = 0; idx < tabList.size(); ++idx) {
			final int i = idx;
			tabList.get(i).setOnMouseClicked((event) -> {setMainTab(TABS[i]);});
		}

		setMainTab(MainTab.DASHBOARD);

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
    }*/

	/*
	 * Correct way of switching tabs
	 */
	private void setMainTab(MainTab selection) {
		if (selection == currentTab) return;

		if (currentTab != MainTab.SETTINGS) tabList.get(TABS_MAP.get(currentTab)).getStyleClass().remove("selectedSideBarItem");
		if (selection != MainTab.SETTINGS) tabList.get(TABS_MAP.get(selection)).getStyleClass().add("selectedSideBarItem");
		currentTab = selection;

		String fxmlName = null;
		switch (selection) {
			case DASHBOARD: fxmlName = "Dashboard"; break;
			case CATEGORIES: fxmlName = "Categories"; break;
			case EXPENSES: fxmlName = "Expenses"; break;
			case SETTINGS: fxmlName = "Settings"; break;
			default: break;
		}

		Node tab = App.loadFXML(fxmlName);
		mainTab.getChildren().clear();
		mainTab.getChildren().add(tab);
		
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
