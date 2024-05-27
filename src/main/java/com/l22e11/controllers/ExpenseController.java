package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import com.l22e11.helper.ExpenseFieldValidation;
import com.l22e11.helper.SideTab;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.LoginFieldValidation;

import model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import model.Category;
import model.Charge;

public class ExpenseController implements Initializable {

	@FXML
	private AnchorPane expenseNameBack, expenseDescriptionBack, expenseCategoryBack, expenseCostBack, expenseUnitsBack, expenseDateBack;
	@FXML
	private TextField expenseName, expenseCost;
	@FXML
	private TextArea expenseDescription;
	@FXML
	private ComboBox<Category> expenseCategory;
	@FXML
	private Spinner<Integer> expenseUnits;
	@FXML
	private DatePicker expenseDate;
	@FXML
	private ImageView invoiceView;
	@FXML
	private Label expenseNameError, expenseDescriptionError, expenseCategoryError, expenseCostError, expenseUnitsError, expenseDateError, expenseImageError;

	public static Charge currentCharge;
	public static ObservableList<Category> categories;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		ExpenseFieldValidation.expenseBoxes = new TextInputControl[]{expenseName, expenseDescription, expenseCost, null, null, null, null};
		ExpenseFieldValidation.expenseBoxesBack = new AnchorPane[]{expenseNameBack, expenseDescriptionBack, expenseCostBack, expenseCategoryBack, expenseUnitsBack, expenseDateBack, null};
		ExpenseFieldValidation.expenseErrorMessages = new Label[]{expenseNameError, expenseDescriptionError, expenseCostError, expenseCategoryError, expenseUnitsError, expenseDateError, expenseImageError};

		// INITIALIZER OF COMBO BOX
		List<Category> list = AccountWrapper.getUserCategories();
        categories = FXCollections.observableList(list);
        for(Category category : categories){
            expenseCategory.getItems().add(category);

        }

		ExpenseFieldValidation.expenseCategory = expenseCategory;
		ExpenseFieldValidation.expenseUnits = expenseUnits;
		ExpenseFieldValidation.expenseDate = expenseDate;
		ExpenseFieldValidation.invoiceView = invoiceView;

		for (int idx = ExpenseFieldValidation.EXPENSE_NAME_IDX; idx <= ExpenseFieldValidation.EXPENSE_COST_IDX; ++idx) {
			ExpenseFieldValidation.setFocusListener(idx);
			ExpenseFieldValidation.setTabSimulator(idx);
		}

		ExpenseFieldValidation.setFocusListenerByElement(expenseCategory, ExpenseFieldValidation.EXPENSE_CATEGORY_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseCategory);
		ExpenseFieldValidation.setFocusListenerByElement(expenseUnits, ExpenseFieldValidation.EXPENSE_UNIT_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseUnits);
		ExpenseFieldValidation.setFocusListenerByElement(expenseDate, ExpenseFieldValidation.EXPENSE_DATE_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseDate);

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2000000000, 1, 1);
        ExpenseFieldValidation.expenseUnits.setValueFactory(valueFactory);
    }

	@FXML
	private void onDiscardChanges(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard this expense?");
		if (alert.showAndWait().isPresent()) {
			currentCharge = null;
			MainController.setSideTab(SideTab.NONE);
		}
	}

	@FXML
	private void onSaveChanges(ActionEvent event) {
		ExpenseFieldValidation.checkExpenseFields();

		if (ExpenseFieldValidation.checkExpenseFields()) {
			boolean isOk = ExpenseFieldValidation.validateExpense();
			if (isOk){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Confirm Save");
				alert.setHeaderText("");
				alert.setContentText("Changes saved correctly");
				if (alert.showAndWait().isPresent()) {
					// resetFields();
					MainController.reloadSideBar();
				}
			}
			// TODO: Update listview

			currentCharge = null;
			MainController.setSideTab(SideTab.NONE);

		}
		
		// User user = AccountWrapper.getAuthenticatedUser();

		
		// if (LoginFieldValidation.checkRegisterFields()) {
		// 	AccountWrapper.updateUser(user,
		// 	LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_NAME_IDX].getText(),
		// 	LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_SURNAME_IDX].getText(),
		// 	LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_EMAIL_IDX].getText(),
		// 	LoginFieldValidation.authenticationBoxes[LoginFieldValidation.REGISTER_PASS_IDX].getText(),
		// 	LoginFieldValidation.authenticationProfileImage.getImage()
		// 	);

		// 	Alert alert = new Alert(AlertType.CONFIRMATION);
		// 	alert.setTitle("Confirm Save");
		// 	alert.setHeaderText("Save Changes");
		// 	alert.setContentText("Are you sure you want to save your changes?");
		// 	if (alert.showAndWait().isPresent()) {
		// 		// resetFields();
		// 		MainController.reloadSideBar();
		// 	}
		// }
	}
}
