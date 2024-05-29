package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.helper.ExpenseFieldValidation;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ExpenseController implements Initializable {

	@FXML
	private AnchorPane expenseNameBack, expenseDescriptionBack, expenseCategoryBack, expenseCostBack, expenseUnitsBack, expenseDateBack;
	@FXML
	private TextField expenseName, expenseCost, expenseUnits;
	@FXML
	private TextArea expenseDescription;
	@FXML
	private ComboBox<String> expenseCategory;
	@FXML
	private DatePicker expenseDate;
	@FXML
	private ImageView invoiceView;
	@FXML
	private Label expenseNameError, expenseDescriptionError, expenseCategoryError, expenseCostError, expenseUnitsError, expenseDateError, expenseImageError;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		ExpenseFieldValidation.expenseBoxes = new TextInputControl[]{expenseName, expenseDescription, expenseCost, expenseUnits, null, null, null};
		ExpenseFieldValidation.expenseBoxesBack = new AnchorPane[]{expenseNameBack, expenseDescriptionBack, expenseCostBack, expenseUnitsBack, expenseCategoryBack, expenseDateBack, null};
		ExpenseFieldValidation.expenseErrorMessages = new Label[]{expenseNameError, expenseDescriptionError, expenseCostError, expenseUnitsError, expenseCategoryError, expenseDateError, expenseImageError};

		// INITIALIZER OF COMBO BOX
		// expenseCategory.setItems(GlobalState.categoriesObservableList);

		ExpenseFieldValidation.expenseCategory = expenseCategory;
		ExpenseFieldValidation.expenseDate = expenseDate;
		ExpenseFieldValidation.invoiceView = invoiceView;

		for (int idx = ExpenseFieldValidation.EXPENSE_NAME_IDX; idx <= ExpenseFieldValidation.EXPENSE_UNIT_IDX; ++idx) {
			ExpenseFieldValidation.setFocusListener(idx);
			ExpenseFieldValidation.setTabSimulator(idx);
		}

		ExpenseFieldValidation.setFocusListenerByElement(expenseCategory, ExpenseFieldValidation.EXPENSE_CATEGORY_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseCategory);
		ExpenseFieldValidation.setFocusListenerByElement(expenseDate, ExpenseFieldValidation.EXPENSE_DATE_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseDate);
    }

	@FXML
	private void onSaveChanges(ActionEvent event) {
		if (!ExpenseFieldValidation.checkExpenseFields()) return;

		if (ExpenseFieldValidation.validateAndRegisterExpense()) {
			GlobalState.reloadExpenses();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Confirm Save");
			alert.setHeaderText("");
			alert.setContentText("Changes saved correctly");
			if (alert.showAndWait().isPresent()) {
				GlobalState.currentCharge = null;
				MainController.setSideTab(SideTab.NONE);
			}
			// TODO: Update listview

			// GlobalState.currentCharge = null;
			// MainController.setSideTab(SideTab.NONE);
			// // BAD BUT CORRECT IMPLEMENTATION TO REFRESH
			// MainController.setMainTab(MainTab.DASHBOARD);
			// MainController.setMainTab(MainTab.EXPENSES);
		}
	}

	@FXML
	private void onDiscardChanges(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard this expense?");
		if (alert.showAndWait().isPresent()) {
			GlobalState.currentCharge = null;
			MainController.setSideTab(SideTab.NONE);
		}
	}

	// @FXML //TODO
    // private void onRemoveCharge(MouseEvent event) {

	// }
}
