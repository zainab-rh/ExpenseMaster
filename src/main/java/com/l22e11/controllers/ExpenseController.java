package com.l22e11.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.l22e11.App;
import com.l22e11.helper.ExpenseFieldValidation;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;
import com.l22e11.helper.Utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.layout.Pane;
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
	private Label expenseNameError, expenseDescriptionError, expenseCategoryError, expenseCostError, expenseUnitsError, expenseDateError, destructiveLabel, constructiveLabel;
	@FXML
	private HBox expenseDateBox;
	@FXML
	private HBox expenseCategoryBox;
	@FXML
	private Pane invoicePane, invoicePaneCroppable;

	private ImageView expenseInvoiceUncropped;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		ExpenseFieldValidation.expenseBoxes = new TextInputControl[]{expenseName, expenseDescription, expenseCost, expenseUnits, null, null, null};
		ExpenseFieldValidation.expenseBoxesBack = new AnchorPane[]{expenseNameBack, expenseDescriptionBack, expenseCostBack, expenseUnitsBack, expenseCategoryBack, expenseDateBack, null};
		ExpenseFieldValidation.expenseErrorMessages = new Label[]{expenseNameError, expenseDescriptionError, expenseCostError, expenseUnitsError, expenseCategoryError, expenseDateError, null};

		updateCategoryComboBox();
		GlobalState.categoriesObservableList.addListener((ListChangeListener<Category>) change -> {
			updateCategoryComboBox();
		});

		ExpenseFieldValidation.expenseCategory = expenseCategory;
		ExpenseFieldValidation.expenseCategoryBox = expenseCategoryBox;
		ExpenseFieldValidation.expenseDate = expenseDate;
		ExpenseFieldValidation.expenseDateBox = expenseDateBox;
		ExpenseFieldValidation.expenseInvoice = expenseInvoice;
		expenseInvoiceUncropped = new ImageView();
		ExpenseFieldValidation.expenseInvoiceUncropped = expenseInvoiceUncropped;
		ExpenseFieldValidation.invoicePaneCroppable = invoicePaneCroppable;

		for (int idx = ExpenseFieldValidation.EXPENSE_NAME_IDX; idx <= ExpenseFieldValidation.EXPENSE_UNIT_IDX; ++idx) {
			ExpenseFieldValidation.setFocusListener(idx);
			ExpenseFieldValidation.setTabSimulator(idx);
		}

		ExpenseFieldValidation.setFocusListenerByElement(expenseCategory, expenseCategoryBox, expenseCategoryBack, ExpenseFieldValidation.EXPENSE_CATEGORY_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseCategory);
		ExpenseFieldValidation.setFocusListenerByElement(expenseDate, expenseDateBox, expenseDateBack, ExpenseFieldValidation.EXPENSE_DATE_IDX);
		ExpenseFieldValidation.setTabSimulatorByElement(expenseDate);

		ExpenseFieldValidation.populateFields();

		expenseDateBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			expenseDate.requestFocus();
			expenseDate.show();
		});

		expenseCategoryBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			expenseCategory.requestFocus();
			expenseCategory.show();
		});

		if (GlobalState.currentCharge == null) {
			constructiveLabel.setText("Save Expense");
			destructiveLabel.setText("Discard Expense");
		} else {
			constructiveLabel.setText("Save Changes");
			destructiveLabel.setText("Discard Changes");
		}

		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			expenseInvoice.setImage(Utils.cropImage(expenseInvoiceUncropped.getImage(), invoicePaneCroppable, 30));
			ExpenseFieldValidation.setChangeListeners();
		}), 50, TimeUnit.MILLISECONDS);
    }

	private void updateCategoryComboBox() {
		List<String> categoryOptions = new ArrayList<>();
		for (Category category : GlobalState.categoriesObservableList) {
			categoryOptions.add(category.getName().substring(8));
		}
		expenseCategory.setItems(FXCollections.observableList(categoryOptions));
	}

	@FXML
	private void onBrowseInvoice() {
		Image uncroppedImage = Utils.loadNewPicture();
		if (uncroppedImage == null) return;
		Image croppedImage = Utils.cropImage(uncroppedImage, invoicePaneCroppable, 30);
        if (croppedImage == null) return;
		ExpenseFieldValidation.expenseInvoice.setImage(croppedImage);
		ExpenseFieldValidation.expenseInvoiceUncropped.setImage(uncroppedImage);
	}

	@FXML
	private void onResetInvoice() {
		ExpenseFieldValidation.expenseInvoice.setImage(App.onePixelTransparent);
		ExpenseFieldValidation.expenseInvoiceUncropped.setImage(App.onePixelTransparent);
	}

	@FXML
	private void onCreateNewCategory(ActionEvent event) {
		GlobalState.currentCategory = null;
		MainController.openSideTab(SideTab.CREATE_CATEGORY);
	}

	@FXML
	private void onSaveChanges(ActionEvent event) {
		if (!ExpenseFieldValidation.checkExpenseFields()) return;

		if (ExpenseFieldValidation.registerOrUpdateExpense()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Expense Saved");
			alert.setHeaderText("Expense Saved");
			alert.setContentText("Expense saved correctly");
			if (alert.showAndWait().isPresent()) {
				GlobalState.currentCharge = null;
				GlobalState.changesInCurrentCharge = false;
				MainController.closeSideTab();
			}
		}
	}

	@FXML
	private void onDiscardChanges(ActionEvent event) {
		if (requestDiscardChanges()) MainController.closeSideTab();
	}

	public static boolean requestDiscardChanges() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Discard");
		alert.setHeaderText("Discard Changes");
		alert.setContentText("Are you sure you want to discard your changes?");
		if (GlobalState.changesInCurrentCharge == false || alert.showAndWait().get() == ButtonType.OK) {
			GlobalState.currentCharge = null;
			GlobalState.changesInCurrentCharge = false;
			return true;
		}
		return false;
	}
}
