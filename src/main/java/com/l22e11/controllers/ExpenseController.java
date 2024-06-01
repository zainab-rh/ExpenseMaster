package com.l22e11.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.helper.ExpenseFieldValidation;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.Category;

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
	private ImageView expenseInvoice;
	@FXML
	private Label expenseNameError, expenseDescriptionError, expenseCategoryError, expenseCostError, expenseUnitsError, expenseDateError, expenseImageError, destructiveLabel, constructiveLabel, expenseID;
	@FXML
	private HBox expenseDateBox;
	@FXML
	private HBox expenseCategoryBox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		ExpenseFieldValidation.expenseBoxes = new TextInputControl[]{expenseName, expenseDescription, expenseCost, expenseUnits, null, null, null};
		ExpenseFieldValidation.expenseBoxesBack = new AnchorPane[]{expenseNameBack, expenseDescriptionBack, expenseCostBack, expenseUnitsBack, expenseCategoryBack, expenseDateBack, null};
		ExpenseFieldValidation.expenseErrorMessages = new Label[]{expenseNameError, expenseDescriptionError, expenseCostError, expenseUnitsError, expenseCategoryError, expenseDateError, expenseImageError};

		List<String> categoryOptions = new ArrayList<>();
		for (Category category : GlobalState.categoriesObservableList) {
			categoryOptions.add(category.getName());
		}
		expenseCategory.setItems(FXCollections.observableList(categoryOptions));

		ExpenseFieldValidation.expenseCategory = expenseCategory;
		ExpenseFieldValidation.expenseCategoryBox = expenseCategoryBox;
		ExpenseFieldValidation.expenseDate = expenseDate;
		ExpenseFieldValidation.expenseDateBox = expenseDateBox;
		ExpenseFieldValidation.expenseInvoice = expenseInvoice;

		for (int idx = ExpenseFieldValidation.EXPENSE_NAME_IDX; idx <= ExpenseFieldValidation.EXPENSE_UNIT_IDX; ++idx) {
			ExpenseFieldValidation.setFocusListener(idx);
			ExpenseFieldValidation.setTabSimulator(idx);
		}

		ExpenseFieldValidation.setFocusListenerByElement(expenseCategory, expenseCategoryBox, expenseCategoryBack, ExpenseFieldValidation.EXPENSE_CATEGORY_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseCategory);
		ExpenseFieldValidation.setFocusListenerByElement(expenseDate, expenseDateBox, expenseDateBack, ExpenseFieldValidation.EXPENSE_DATE_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseDate);

		ExpenseFieldValidation.populateFields();
		ExpenseFieldValidation.setChangeListeners();

		expenseDateBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			expenseDate.requestFocus();
			expenseDate.show();
		});

		expenseCategoryBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			expenseCategory.requestFocus();
			expenseCategory.show();
		});

		if (GlobalState.currentCharge == null) {
			expenseID.setText("None");
			constructiveLabel.setText("Save Expense");
			destructiveLabel.setText("Discard Expense");
		} else {
			expenseID.setText(String.valueOf(GlobalState.currentCharge.getId()));
			constructiveLabel.setText("Save Changes");
			destructiveLabel.setText("Discard Changes");
		}
    }

	@FXML
	private void onSaveChanges(ActionEvent event) {
		if (!ExpenseFieldValidation.checkExpenseFields()) return;

		if (ExpenseFieldValidation.validateAndRegisterExpense()) {
			GlobalState.reloadExpenses();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Expense Saved");
			alert.setHeaderText("Expense Saved");
			alert.setContentText("Expense saved correctly");
			if (alert.showAndWait().isPresent()) {
				// TODO: Add charge
				GlobalState.currentCharge = null;
				MainController.setSideTab(SideTab.NONE);
			}
		}
	}

	@FXML
	private void onDiscardChanges(ActionEvent event) {
		requestDiscardChanges();
	}

	public static boolean requestDiscardChanges() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard your changes?");
		if (GlobalState.sideTabModified == false || alert.showAndWait().get() == ButtonType.OK) {
			GlobalState.currentCharge = null;
			GlobalState.sideTabModified = false;
			MainController.setSideTab(SideTab.NONE);
			return true;
		}
		return false;
	}

	// @FXML //TODO
    // private void onRemoveCharge(MouseEvent event) {

	// }
}
