package com.l22e11.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.SideTab;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Category;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class CategoriesController implements Initializable {

    @FXML
	private HBox bigAddButton;
    @FXML
    private ListView<AnchorPane> categoriesListView;

	public static boolean firstTime = true;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		reloadList();
		if (firstTime) {
			GlobalState.categoriesObservableList.addListener((ListChangeListener<Category>) change -> {
				reloadList();
			});
		}
		// firstTime = false;

        bigAddButton.setOnMouseClicked((event) -> {
			GlobalState.currentCategory = null;
			MainController.openSideTab(SideTab.CREATE_CATEGORY);
		});
	}

	private void reloadList() {
		System.out.println("Reloaded");
		// if (GlobalState.categoriesObservableList.contains(null)) return;

		List<AnchorPane> listItems = new ArrayList<>();
		for (Category category : GlobalState.categoriesObservableList) {
			AnchorPane pane = new AnchorPane(); pane.getStyleClass().add("itemBack");
			HBox listItem = new HBox(); listItem.getStyleClass().add("categoryItem");
			VBox nameAndDescription = new VBox(); nameAndDescription.getStyleClass().add("nameAndDescription");
			Label name = new Label(); name.getStyleClass().add("itemName");
			Label description = new Label(); description.getStyleClass().add("itemDescription");
			Region spacer = new Region();
			VBox icons = new VBox();
			Label modifyIcon = new Label(); modifyIcon.getStyleClass().add("itemIcon");
			Label deleteIcon = new Label(); deleteIcon.getStyleClass().addAll("itemIcon", "itemDelete");

			pane.prefWidthProperty().bind(categoriesListView.widthProperty().subtract(16));
			AnchorPane.setTopAnchor(listItem, 4.0);
			AnchorPane.setRightAnchor(listItem, 4.0);
			AnchorPane.setBottomAnchor(listItem, 4.0);
			AnchorPane.setLeftAnchor(listItem, 24.0);
			HBox.setHgrow(spacer, Priority.ALWAYS);
			
			pane.setStyle("-fx-background-color: #" + category.getName().substring(0, 8));
			pane.setOnMouseClicked((event) -> {
				clickedOnCategory(category);
			});
			name.setText(category.getName().substring(8));
			description.setText(category.getDescription());
			VBox.setVgrow(description, Priority.ALWAYS);
			description.setWrapText(true);
			modifyIcon.setText("");
            modifyIcon.setOnMouseClicked((event) -> {
				event.consume();
                clickedModifyOnCategory(category);
            });
			deleteIcon.setText("");
            deleteIcon.setOnMouseClicked((event) -> {
				event.consume();
                clickedDeleteOnCategory(category);
            });

			nameAndDescription.getChildren().addAll(name, description);
			icons.getChildren().addAll(modifyIcon, deleteIcon);
			listItem.getChildren().addAll(nameAndDescription, spacer, icons);
			pane.getChildren().add(listItem);
			listItems.add(pane);

		}
		categoriesListView.setItems(FXCollections.observableList(listItems));
		categoriesListView.setMinHeight(listItems.size() * (12 + 108));
	}

	private void clickedOnCategory(Category category) {
        // TODO: What happens if a user clicks on a category?
		System.out.println(category.getName());
    }

    private void clickedModifyOnCategory(Category category) {
        if (MainController.closeAllTabs()) {
            GlobalState.currentCategory = category;
            MainController.openSideTab(SideTab.UPDATE_CATEGORY);
        }
    }

    private void clickedDeleteOnCategory(Category category) {
		if (MainController.closeAllTabs()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Category");
			alert.setHeaderText("Delete Category");
			alert.setContentText("Are you sure you want to delete the \"" + category.getName().substring(8) + "\" category?");
			alert.setResizable(true);
            alert.setWidth(600);
            alert.setHeight(400);
			if (alert.showAndWait().get() == ButtonType.OK) {
				AccountWrapper.removeCategory(category);
			}
		}
    }
}