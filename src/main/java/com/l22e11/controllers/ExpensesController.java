package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListView;

public class ExpensesController implements Initializable {

	@FXML
	private HBox bigAddButton;
	@FXML
    private ListView<HBox> chargesListView;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		// chargesListView.setItems(GlobalState.expensesObservableList);

		bigAddButton.setOnMouseClicked((event) -> {
			GlobalState.currentCharge = null;
			MainController.setSideTab(SideTab.MANAGE_EXPENSE);
		});
	}
}
