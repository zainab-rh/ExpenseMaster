package com.l22e11.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Colors;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;
import com.l22e11.helper.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Charge;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;

public class ExpensesController implements Initializable {

	@FXML
	private HBox bigAddButton;
	@FXML
    private ListView<AnchorPane> chargesListView;
	public static boolean firstTime = true;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		reloadList();
		if (firstTime) {
			GlobalState.expensesObservableList.addListener((ListChangeListener<Charge>) change -> {
				reloadList();
			});
		}
		// firstTime = false;

		bigAddButton.setOnMouseClicked((event) -> {
			GlobalState.currentCharge = null;
			MainController.openSideTab(SideTab.CREATE_EXPENSE);
		});
	}

	private void reloadList() {
		// System.out.println("PING");
		// if (GlobalState.expensesObservableList.contains(null)) return;
		// System.out.println("PONG");

		List<AnchorPane> listItems = new ArrayList<>();
		for (Charge charge : GlobalState.expensesObservableList) {
			AnchorPane pane = new AnchorPane(); pane.getStyleClass().add("itemBack");
			VBox nameAndDescription = new VBox(); nameAndDescription.getStyleClass().add("nameAndDescription");
			AnchorPane categoryPane = new AnchorPane(); categoryPane.getStyleClass().add("categoryLabelPane");
			Label category = new Label(); category.getStyleClass().add("categoryLabel");
			Label name = new Label(); name.getStyleClass().add("itemName");
			Label description = new Label(); description.getStyleClass().add("itemDescription");
			Label date  = new Label(); date.getStyleClass().add("itemDate");
			HBox viewInvoice = new HBox(); viewInvoice.getStyleClass().add("viewInvoice");
			Label eye = new Label(); eye.getStyleClass().add("smallEyeIcon");
			Label viewInvoiceLabel = new Label();
			Label modifyIcon = new Label(); modifyIcon.getStyleClass().add("itemIcon");
			Label deleteIcon = new Label(); deleteIcon.getStyleClass().addAll("itemIcon", "itemDelete");
			Color colour = Color.valueOf("#" + charge.getCategory().getName().substring(0, 8));
			VBox priceBox = new VBox();
			Label units = new Label(); units.getStyleClass().add("priceSmall");
			Label costPerUnit = new Label(); costPerUnit.getStyleClass().add("priceSmall");
			Label totalCost = new Label(); totalCost.getStyleClass().add("priceLarge");

			pane.prefWidthProperty().bind(chargesListView.widthProperty().subtract(16));
			pane.minWidthProperty().bind(chargesListView.widthProperty().subtract(16));
			pane.maxWidthProperty().bind(chargesListView.widthProperty().subtract(16));
			nameAndDescription.prefWidthProperty().bind(pane.widthProperty().subtract(priceBox.widthProperty().add(24)));
			pane.setOnMouseClicked((event) -> {
				clickedOnCharge(charge);
			});

			AnchorPane.setBottomAnchor(nameAndDescription, 0.0);
			AnchorPane.setLeftAnchor(nameAndDescription, 0.0);
			AnchorPane.setTopAnchor(nameAndDescription, 0.0);
			nameAndDescription.setFillWidth(false);

			category.setText(charge.getCategory().getName().substring(8));
			category.setStyle("-fx-text-fill: " + (Utils.getBrightness(colour) > 0.65 ? Colors.PRIMARY_DARK_GREY : Colors.BACKGROUND) + ";");
			categoryPane.setStyle("-fx-background-color: #" + colour.toString().substring(2, 8) + ";");
			categoryPane.getChildren().add(category);
			AnchorPane.setTopAnchor(category, 4.0);
			AnchorPane.setRightAnchor(category, 6.0);
			AnchorPane.setBottomAnchor(category, 4.0);
			AnchorPane.setLeftAnchor(category, 6.0);
			name.setText(charge.getName());
			description.setText(charge.getDescription());
			VBox.setVgrow(description, Priority.ALWAYS);
			description.setWrapText(true);
			date.setText(Utils.localDateToString(charge.getDate()));
			
			eye.setText("");
			viewInvoiceLabel.setText("View Invoice");
			viewInvoice.getChildren().addAll(eye, viewInvoiceLabel);
			AnchorPane.setTopAnchor(viewInvoice, 0.0);
			AnchorPane.setRightAnchor(viewInvoice, 80.0);
			viewInvoice.setOnMouseClicked((event) -> {
				clickedOnViewInvoice(charge);
			});
			
			AnchorPane.setTopAnchor(modifyIcon, 0.0);
			AnchorPane.setRightAnchor(modifyIcon, 40.0);
			modifyIcon.setText("");
            modifyIcon.setOnMouseClicked((event) -> {
				event.consume();
                clickedModifyOnCharge(charge);
            });
			AnchorPane.setTopAnchor(deleteIcon, 0.0);
			AnchorPane.setRightAnchor(deleteIcon, 0.0);
			deleteIcon.setText("");
            deleteIcon.setOnMouseClicked((event) -> {
				event.consume();
                clickedDeleteOnCharge(charge);
            });
			units.setText("Units: " + charge.getUnits());
			costPerUnit.setText("Cost per Unit: " + Utils.toPrice(charge.getCost()) + " €");
			totalCost.setText("- " + Utils.toPrice(charge.getCost() * charge.getUnits()) + " €");
			priceBox.setAlignment(Pos.BOTTOM_RIGHT);
			AnchorPane.setRightAnchor(priceBox, 0.0);
			AnchorPane.setBottomAnchor(priceBox, 0.0);

			priceBox.getChildren().addAll(units, costPerUnit, totalCost);
			nameAndDescription.getChildren().addAll(categoryPane, name, description, date);
			pane.getChildren().addAll(nameAndDescription, modifyIcon, deleteIcon, priceBox);
			if (charge.getImageScan().getWidth() > 1) pane.getChildren().add(viewInvoice);
			listItems.add(pane);
		}
		chargesListView.setItems(FXCollections.observableList(listItems));
		chargesListView.setMinHeight(listItems.size() * (12 + 150));
	}

	private void clickedOnViewInvoice(Charge charge) {
		try {
			FXMLLoader loader = App.getFxmlLoader("ViewInvoice");
			Scene invoiceScene = new Scene(loader.load(), 1000, 600);
			((ImageViewController) loader.getController()).setImage(charge.getImageScan());

			Stage invoiceView = new Stage();
			invoiceView.setTitle("Invoice for charge '" + charge.getName() + "'");
			invoiceView.setScene(invoiceScene);
			invoiceView.sizeToScene();
			invoiceView.show();
		} catch (Exception e) {System.out.println("Could not load ViewInvoice.fxml");}
	}

	private void clickedOnCharge(Charge charge) {
        // TODO: What happens if a user clicks on a category?
		System.out.println(charge.getName());
    }

    private void clickedModifyOnCharge(Charge charge) {
        if (MainController.closeAllTabs()) {
            GlobalState.currentCharge = charge;
            MainController.openSideTab(SideTab.UPDATE_EXPENSE);
        }
    }

    private void clickedDeleteOnCharge(Charge charge) {
		if (MainController.closeAllTabs()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Charge");
			alert.setHeaderText("Delete Charge");
			alert.setContentText("Are you sure you want to delete the \"" + charge.getName() + "\" charge?");
			alert.setResizable(true);
            alert.setWidth(500);
            alert.setHeight(400);
			if (alert.showAndWait().get() == ButtonType.OK) {
				AccountWrapper.removeCharge(charge);
			}
		}
    }
}
