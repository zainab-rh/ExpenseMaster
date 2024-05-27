package com.l22e11.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.SideTab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import model.Charge;

public class ExpensesController implements Initializable {

	@FXML
	private HBox addExpenseButton;
    
	public static List<Charge> charges;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		charges = AccountWrapper.getUserCharges();

		addExpenseButton.setOnMouseClicked((event) -> {
			MainController.setSideTab(SideTab.MANAGE_EXPENSE);
		});
    }
}
