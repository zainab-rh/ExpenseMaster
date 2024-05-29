package com.l22e11.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.helper.GlobalState;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import model.Category;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

public class CategoriesController implements Initializable {

    @FXML
	private Button removeCategory, modifyCategory, addCategory;
    @FXML
    private TextField categoryName;
    @FXML
    private Label categoryNameError;
    @FXML
    private ListView<HBox> categoriesListView;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		reloadList();
	}

	private void reloadList() {
		List<HBox> listItems = new ArrayList<>();
		for (Category category : GlobalState.categoriesObservableList) {
			HBox listItem = new HBox(); listItem.getStyleClass().add("categoryItem");
			VBox nameAndDescription = new VBox(); nameAndDescription.getStyleClass().add("nameAndDescription");
			Label name = new Label(); name.getStyleClass().add("itemName");
			Label description = new Label(); description.getStyleClass().add("itemDescription");
			Region spacer = new Region();
			Label modifyIcon = new Label(); modifyIcon.getStyleClass().add("itemIcon");
			Label deleteIcon = new Label(); deleteIcon.getStyleClass().addAll("itemIcon", "itemDelete");

			HBox.setMargin(listItem, new Insets(16, 16, 16, 16));
			HBox.setHgrow(spacer, Priority.ALWAYS);
			listItem.setPrefWidth(categoriesListView.getWidth());
			name.setText(category.getName());
			// name.setMaxHeight(24);
			description.setText(category.getDescription());
			VBox.setVgrow(description, Priority.ALWAYS);
			description.setWrapText(true);
			modifyIcon.setText("");
			deleteIcon.setText("");

			nameAndDescription.getChildren().addAll(name, description);
			listItem.getChildren().addAll(nameAndDescription, spacer, modifyIcon, deleteIcon);
			listItems.add(listItem);

		}
		categoriesListView.setItems(FXCollections.observableList(listItems));
	}



    // public Category findCategory(String name){
    //     String categoryNameText = categoryName.getText();
    //     for (Category category : GlobalState.categories) {
    //         if (category.getName().equals(categoryNameText)) {
    //             return category;
    //         }
    //     }
    //     return null;
    // }

    @FXML 
    private void onModifyCategory(ActionEvent event){
        // String categoryNameText = categoryName.getText();
        // Category victimCategory = findCategory(categoryNameText);

        // GlobalState.currentCategory = victimCategory;
        // MainController.setSideTab(SideTab.MANAGE_CATEGORY);
    }

    @FXML 
    private void onAddCategory(ActionEvent event){
        // String categoryNameText = categoryName.getText();
        // Category victimCategory = findCategory(categoryNameText);

        // GlobalState.currentCategory = victimCategory;
        // MainController.setSideTab(SideTab.MANAGE_CATEGORY);
    }

    @FXML //TODO
    private void onRemoveCategory(MouseEvent event) {
        // String categoryNameText = categoryName.getText();
        // Category victimCategory = findCategory(categoryNameText);

        // if (victimCategory!= null) {
        //     if (AccountWrapper.removeCategory(victimCategory)) {
        //         categoryName.clear();
        //         categoryNameError.setVisible(false);
        //         Alert alert = new Alert(AlertType.INFORMATION);
        //         alert.setTitle("Category remover");
        //         alert.setHeaderText("");
        //         alert.setContentText("Category removed correctly");
        //         if (alert.showAndWait().isPresent()) {
        //             MainController.setSideTab(SideTab.NONE);

        //             // BAD BUT CORRECT IMPLEMENTATION TO REFRESH
        //             MainController.setMainTab(MainTab.DASHBOARD);
        //             MainController.setMainTab(MainTab.CATEGORIES);

        //         }
                
        //     } else {
        //         categoryNameError.setText("Error removing category");
        //         categoryNameError.setVisible(true);
        //     }
        // } else {
        //     categoryNameError.setText("Category not found");
        //     categoryNameError.setVisible(true);
        // }
    }
}