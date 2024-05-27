package com.l22e11.helper;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class CategoryFieldValidation {
    public static final int CATEGORY_NAME_IDX = 0, CATEGORY_DESCRIPTION_IDX = 1;

    public static final String NAME_ERROR = "A category with this name already exists";
	public static final String EMPTY_ERROR = "Field empty";

	public static Label categoryErrorMessages[];
    public static TextInputControl categoryBoxes[];
    public static AnchorPane categoryBoxesBack[];

	public static void setInputBoxColor(int i, boolean active, String color) {
		categoryBoxes[i].setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
		categoryBoxesBack[i].setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
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

	public static boolean validateField(int i) {
        switch (i) {
            case CATEGORY_NAME_IDX:         return validateName();
            case CATEGORY_DESCRIPTION_IDX:  return validateDescription();
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

	public static boolean checkCategoryFields() {
		boolean allGood = true;
		allGood &= validateName();
        allGood &= validateDescription();
		return allGood;
    }
	
	public static boolean validateCategory() {
		String name = categoryBoxes[CATEGORY_NAME_IDX].getText();
        String description = categoryBoxes[CATEGORY_DESCRIPTION_IDX].getText();
        
		boolean isOk = AccountWrapper.registerCategory(name, description);

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

        return isOk;
	}
}
