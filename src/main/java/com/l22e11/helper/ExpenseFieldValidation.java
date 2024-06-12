package com.l22e11.helper;

import java.time.LocalDate;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Category;

public class ExpenseFieldValidation {

	public static final int EXPENSE_NAME_IDX = 0, EXPENSE_DESCRIPTION_IDX = 1, EXPENSE_COST_IDX = 2, EXPENSE_UNIT_IDX = 3, EXPENSE_CATEGORY_IDX = 4, EXPENSE_DATE_IDX = 5, EXPENSE_INVOICE_IDX = 6;
	
	public static final String EMPTY_ERROR = "Field empty";
	public static final String NOT_A_NUMBER = "Not a valid number";
	public static final String ZERO_ERROR = "This has to be more than 0";

	public static Label expenseErrorMessages[];
    public static TextInputControl expenseBoxes[];
    public static AnchorPane expenseBoxesBack[];
	public static ComboBox<String> expenseCategory;
	public static DatePicker expenseDate;
	public static HBox expenseDateBox;
	public static HBox expenseCategoryBox;
	public static ImageView expenseInvoice;
	public static ImageView expenseInvoiceUncropped;
	public static Pane invoicePaneCroppable;

	public static void setInputBoxColor(int i, boolean active, String color) {
		expenseBoxes[i].setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
		expenseBoxesBack[i].setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
    }

	public static void setInputBoxColor(Node node, Node nodeBack, boolean active, String color) {
		node.setStyle("-fx-border-color: " + (active ? color + ";" : Colors.PRIMARY_DARK_GREY_SOFT));
		nodeBack.setStyle("-fx-background-color: " + (active ? color + "-soft;" : "transparent;"));
    }

	public static void setFocusListener(int i) {
		expenseBoxes[i].focusedProperty().addListener((obs, oldV, newV) ->  {
			if (newV.booleanValue()) { setInputBoxColor(i, true, Colors.BLUE_ACCENT); }
			else {
				if (expenseBoxes[i].getText().length() == 0) { setInputBoxColor(i, false, ""); }
				else { validateField(i); }
			}
		});
	}

	public static void setFocusListenerByElement(Node element, Node elementBox, Node elementBack, int i) {
		element.focusedProperty().addListener((obs, oldV, newV) ->  {
			if (newV.booleanValue()) { setInputBoxColor(elementBox, elementBack, true, Colors.BLUE_ACCENT); }
			else { validateField(i); }
		});
	}

	public static void setTabSimulator(int i) {
		expenseBoxes[i].setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				KeyEvent tabKeyEvent = new KeyEvent(
					KeyEvent.KEY_PRESSED,
					KeyCode.TAB.getChar(),
					KeyCode.TAB.getName(),
					KeyCode.TAB,
					false, false, false, false
				);
				expenseBoxes[i].fireEvent(tabKeyEvent);
			}
		});
	}

	public static void setTabSimulatorByElement(Control element) {
		element.setOnKeyPressed((event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				KeyEvent tabKeyEvent = new KeyEvent(
					KeyEvent.KEY_PRESSED,
					KeyCode.TAB.getChar(),
					KeyCode.TAB.getName(),
					KeyCode.TAB,
					false, false, false, false
				);
				element.fireEvent(tabKeyEvent);
			}
		});
	}

	public static boolean validateField(int i) {
        switch (i) {
			case EXPENSE_NAME_IDX:         return validateName();
			case EXPENSE_DESCRIPTION_IDX:  return validateDescription();
			case EXPENSE_COST_IDX:         return validateCost();
			case EXPENSE_CATEGORY_IDX:     return validateCategory();
			case EXPENSE_UNIT_IDX:         return validateUnit();
			case EXPENSE_DATE_IDX:         return validateDate();
			case EXPENSE_INVOICE_IDX:      return validateInvoice();
        }
		return false;
    }

    public static boolean checkIfEmpty(int i) {
        boolean isEmpty = expenseBoxes[i].getText().length() == 0;

        expenseErrorMessages[i].setText(EMPTY_ERROR);
        expenseErrorMessages[i].setVisible(true);

        setInputBoxColor(i, true, Colors.RED_ACCENT);

        return isEmpty;
    }

	public static boolean validateName() {
		if (checkIfEmpty(EXPENSE_NAME_IDX)) return false;

		expenseErrorMessages[EXPENSE_NAME_IDX].setVisible(false);
		setInputBoxColor(EXPENSE_NAME_IDX, true, Colors.GREEN_ACCENT);
		return true;
	}

	public static boolean validateDescription() {
		if (checkIfEmpty(EXPENSE_DESCRIPTION_IDX)) return false;

		expenseErrorMessages[EXPENSE_DESCRIPTION_IDX].setVisible(false);
		setInputBoxColor(EXPENSE_DESCRIPTION_IDX, true, Colors.GREEN_ACCENT);
		return true;
	}

	public static boolean validateCost() {
		if (checkIfEmpty(EXPENSE_COST_IDX)) return false;

		boolean allGood = true;
		String textToCheck = expenseBoxes[EXPENSE_COST_IDX].getText().replaceAll(",", ".");
		double valueEquivalent = 0;
		
		try {
			valueEquivalent = Double.parseDouble(textToCheck);
			expenseBoxes[EXPENSE_COST_IDX].setText(Utils.toPrice(valueEquivalent).replaceAll(",", "."));
		} catch (NumberFormatException e) {
			expenseErrorMessages[EXPENSE_COST_IDX].setText(NOT_A_NUMBER);
			allGood = false;
		}

		if (allGood && valueEquivalent <= 0) {
			expenseErrorMessages[EXPENSE_COST_IDX].setText(ZERO_ERROR);
			allGood = false;
		}

		expenseErrorMessages[EXPENSE_COST_IDX].setVisible(!allGood);
		setInputBoxColor(EXPENSE_COST_IDX, true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
		return allGood;
	}

	public static boolean validateUnit() {
		if (checkIfEmpty(EXPENSE_UNIT_IDX)) return false;

		boolean allGood = true;
		String textToCheck = expenseBoxes[EXPENSE_UNIT_IDX].getText().replaceAll(",", ".");
		int valueEquivalent = 0;
		
		try {
			valueEquivalent = Integer.parseInt(textToCheck);
			expenseBoxes[EXPENSE_UNIT_IDX].setText(String.valueOf(valueEquivalent));
		} catch (NumberFormatException e) {
			expenseErrorMessages[EXPENSE_UNIT_IDX].setText(NOT_A_NUMBER);
			allGood = false;
		}

		if (allGood && valueEquivalent <= 0) {
			expenseErrorMessages[EXPENSE_UNIT_IDX].setText(ZERO_ERROR);
			allGood = false;
		}

		expenseErrorMessages[EXPENSE_UNIT_IDX].setVisible(!allGood);
		setInputBoxColor(EXPENSE_UNIT_IDX, true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
		return allGood;
	}

	public static boolean validateCategory() {
		boolean allGood = true;

		if (allGood && expenseCategory.getValue() == null) {
			expenseErrorMessages[EXPENSE_CATEGORY_IDX].setText(EMPTY_ERROR);
			allGood = false;
		}

		expenseErrorMessages[EXPENSE_CATEGORY_IDX].setVisible(!allGood);
		setInputBoxColor(expenseCategoryBox, expenseBoxesBack[EXPENSE_CATEGORY_IDX], true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
		return allGood;
	}

	public static boolean validateDate() {
		boolean allGood = true;

		LocalDate date = expenseDate.getValue();
		
		if (allGood && date == null) {
			expenseErrorMessages[EXPENSE_DATE_IDX].setText(EMPTY_ERROR);
			allGood = false;
		}

		expenseErrorMessages[EXPENSE_DATE_IDX].setVisible(!allGood);
		setInputBoxColor(expenseDateBox, expenseBoxesBack[EXPENSE_DATE_IDX], true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
		return allGood;
	}

	public static boolean validateInvoice() {
		// boolean allGood = true;

		// expenseErrorMessages[EXPENSE_INVOICE_IDX].setVisible(!allGood);
		// setInputBoxColor(EXPENSE_INVOICE_IDX, true, (allGood ? Colors.GREEN_ACCENT : Colors.RED_ACCENT));
		return true;
	}

	public static void populateFields() {
		if (GlobalState.currentCharge == null) return;
		expenseBoxes[EXPENSE_NAME_IDX].setText(GlobalState.currentCharge.getName());
		expenseBoxes[EXPENSE_DESCRIPTION_IDX].setText(GlobalState.currentCharge.getDescription());
		expenseBoxes[EXPENSE_COST_IDX].setText(String.valueOf(GlobalState.currentCharge.getCost()));
		expenseBoxes[EXPENSE_UNIT_IDX].setText(String.valueOf(GlobalState.currentCharge.getUnits()));
		expenseCategory.setValue(GlobalState.currentCharge.getCategory().getName().substring(8));
		expenseDate.setValue(GlobalState.currentCharge.getDate());
		expenseInvoiceUncropped.setImage(GlobalState.currentCharge.getImageScan());
	}

	public static void setChangeListeners() {
		expenseBoxes[EXPENSE_NAME_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCharge = true;
		});
		expenseBoxes[EXPENSE_DESCRIPTION_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCharge = true;
		});
		expenseBoxes[EXPENSE_COST_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCharge = true;
		});
		expenseBoxes[EXPENSE_UNIT_IDX].textProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCharge = true;
		});
		expenseCategory.valueProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCharge = true;
		});
		expenseDate.valueProperty().addListener((obs, oldV, newV) -> {
			GlobalState.changesInCurrentCharge = true;
		});
		expenseInvoice.imageProperty().addListener((obs, oldV, newVs) -> {
			GlobalState.changesInCurrentCharge = true;
		});
	}

	public static boolean checkExpenseFields() {
		boolean allGood = true;
		allGood &= validateName();
        allGood &= validateDescription();
		allGood &= validateCategory();
        allGood &= validateCost();
		allGood &= validateUnit();
        allGood &= validateDate();
		allGood &= validateInvoice();
		return allGood;
    }
	
	public static boolean registerOrUpdateExpense() {
		String name = expenseBoxes[EXPENSE_NAME_IDX].getText();
		String description = expenseBoxes[EXPENSE_DESCRIPTION_IDX].getText();
		double cost = Double.parseDouble(expenseBoxes[EXPENSE_COST_IDX].getText());
		Category category = Utils.getCategoryByName(expenseCategory.getValue());
		int unit = Integer.parseInt(expenseBoxes[EXPENSE_UNIT_IDX].getText());
		LocalDate date = expenseDate.getValue();
		Image invoice = expenseInvoiceUncropped.getImage();
        
		boolean isOk;
		if (GlobalState.currentCharge == null) {
			isOk = AccountWrapper.registerCharge(name, description, cost, unit, invoice, date, category);
		} else {
			isOk = AccountWrapper.updateCharge(name, description, cost, unit, invoice, date, category);
		}

		if (!isOk) {
			System.out.println("ExpenseValidationField couldn't add charge");
		}

        return isOk;
	}
}
