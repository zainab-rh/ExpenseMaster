package com.l22e11.helper;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class CategoryFieldValidation {
    public static final int CATEGORY_NAME_IDX = 0, CATEGORY_DESCRIPTION_IDX = 1, CATEGORY_COLOUR_IDX = 2;

    public static final String NAME_ERROR = "A category with this name already exists";
	public static final String EMPTY_ERROR = "Field empty";

	public static Label categoryErrorMessages[];
    public static TextInputControl categoryBoxes[];
    public static AnchorPane categoryBoxesBack[];
	public static ColorPicker colourPicker;

	public static void setInputBoxColor(int i, boolean active, String color) {
		categoryBoxes[i].setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
		categoryBoxesBack[i].setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
    }

	public static void setColourPickerBoxColor(Color color) {
		colourPicker.setStyle("-fx-border-color: #" + color.toString().substring(2) + ";");
		categoryBoxesBack[CATEGORY_COLOUR_IDX].setStyle("-fx-background-color: #" + color.toString().substring(2) + ";");
    }

	public static void setFocusListener(int i) {
		categoryBoxes[i].focusedProperty().addListener((obs, oldV, newV) ->  {
			if (newV.booleanValue()) { setInputBoxColor(i, true, Colors.BLUE_ACCENT); }
			else {
				if (categoryBoxes[i].getText().length() == 0) { setInputBoxColor(i, false, ""); }
				else { validateField(i); }
			}
		});
	}

	public static void setColourPickerFocusListener() {
		colourPicker.focusedProperty().addListener((obs, oldV, newV) ->  {
			if (newV.booleanValue()) { setColourPickerBoxColor(Color.valueOf("#A8ADFD")); }
			else { setColourPickerBoxColor(colourPicker.getValue()); }
		});
	}

	public static void setTabSimulator(int i) {
		categoryBoxes[i].setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				KeyEvent tabKeyEvent = new KeyEvent (
					KeyEvent.KEY_PRESSED,
					KeyCode.TAB.getChar(),
					KeyCode.TAB.getName(),
					KeyCode.TAB,
					false, false, false, false
				);
				categoryBoxes[i].fireEvent(tabKeyEvent);
			}
		});
	}

	public static void setColourPickerTabSimulator() {
		colourPicker.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				KeyEvent tabKeyEvent = new KeyEvent (
					KeyEvent.KEY_PRESSED,
					KeyCode.TAB.getChar(),
					KeyCode.TAB.getName(),
					KeyCode.TAB,
					false, false, false, false
				);
				colourPicker.fireEvent(tabKeyEvent);
			}
		});
	}

	public static boolean validateField(int i) {
        switch (i) {
            case CATEGORY_NAME_IDX:         return validateName();
            case CATEGORY_DESCRIPTION_IDX:  return validateDescription();
			case CATEGORY_COLOUR_IDX:       return validateColour();
        }
		return false;
    }

    public static boolean checkIfEmpty(int i) {
        boolean isEmpty = categoryBoxes[i].getText().length() == 0;

        categoryErrorMessages[i].setText(EMPTY_ERROR);
        categoryErrorMessages[i].setVisible(true);

        setInputBoxColor(i, true, Colors.RED_ACCENT);

        return isEmpty;
    }

    public static boolean validateName() {
        if (checkIfEmpty(CATEGORY_NAME_IDX)) return false;
    
        categoryErrorMessages[CATEGORY_NAME_IDX].setVisible(false);
        setInputBoxColor(CATEGORY_NAME_IDX, true, Colors.GREEN_ACCENT);
    
        return true;
    }
    
    public static boolean validateDescription() {
        if (checkIfEmpty(CATEGORY_DESCRIPTION_IDX)) return false;
    
        categoryErrorMessages[CATEGORY_DESCRIPTION_IDX].setVisible(false);
        setInputBoxColor(CATEGORY_DESCRIPTION_IDX, true, Colors.GREEN_ACCENT);
    
        return true;
    }

	public static boolean validateColour() {
        setColourPickerBoxColor(colourPicker.getValue());
    
        return true;
    }

	public static void populateFields() {
		setColourPickerBoxColor(Color.WHITE);
		if (GlobalState.currentCategory == null) return;
		categoryBoxes[CATEGORY_NAME_IDX].setText(GlobalState.currentCategory.getName().substring(8));
		categoryBoxes[CATEGORY_DESCRIPTION_IDX].setText(GlobalState.currentCategory.getDescription());
		colourPicker.setValue(Color.valueOf("#" + GlobalState.currentCategory.getName().substring(0, 8)));
		setColourPickerBoxColor(colourPicker.getValue());
	}

	public static void setChangeListeners() {
		categoryBoxes[CATEGORY_NAME_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCategory = true;
		});
		categoryBoxes[CATEGORY_DESCRIPTION_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCategory = true;
		});
		colourPicker.valueProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCategory = true;
		});
	}

	public static boolean checkCategoryFields() {
		boolean allGood = true;
		allGood &= validateName();
        allGood &= validateDescription();
		allGood &= validateColour();
		return allGood;
    }
	
	public static boolean registerOrUpdateCategory() {
		String name = categoryBoxes[CATEGORY_NAME_IDX].getText();
        String description = categoryBoxes[CATEGORY_DESCRIPTION_IDX].getText();
		name = colourPicker.getValue().toString().substring(2) + name;
        
		boolean isOk;
		if (GlobalState.currentCategory == null) {
			isOk = AccountWrapper.registerCategory(name, description);
		} else {
			isOk = AccountWrapper.updateCategory(name, description);
		}

		if (!isOk) {
			categoryErrorMessages[CATEGORY_NAME_IDX].setText(NAME_ERROR);
			categoryErrorMessages[CATEGORY_NAME_IDX].setVisible(!isOk);
			setInputBoxColor(CATEGORY_NAME_IDX, true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));

			return false;
		}
		
		categoryErrorMessages[CATEGORY_NAME_IDX].setVisible(false);
        setInputBoxColor(CATEGORY_DESCRIPTION_IDX, true, (isOk ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
        
		categoryBoxes[CATEGORY_NAME_IDX].clear();
		categoryBoxes[CATEGORY_DESCRIPTION_IDX].clear();
		colourPicker.setValue(Color.WHITE);

        return isOk;
	}
}
