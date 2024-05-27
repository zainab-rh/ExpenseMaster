package com.l22e11.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.SideTab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Charge;

public class ExpensesController implements Initializable {

	@FXML
	private HBox addExpenseButton;
	@FXML
    private ListView<String> chargesListView; //CHANGE TO CHARGE IF NEEDED
    
	public static ObservableList<Charge> charges;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        List<Charge> list = AccountWrapper.getUserCharges();
        charges = FXCollections.observableList(list);

		for(Charge charge : charges){
            chargesListView.getItems().add(charge.getName());
        }

		// for(Charge charge : charges){
        //     chargesListView.getItems().add(charge);
        // }

		addExpenseButton.setOnMouseClicked((event) -> {
			MainController.setSideTab(SideTab.MANAGE_EXPENSE);
		});
	}
}
