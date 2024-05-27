package com.l22e11.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.helper.AccountWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Category;

public class CategoriesController implements Initializable {

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

	}
	
    @FXML //TODO
    private void onRemoveCategory(ActionEvent event) {
        // String categoryNameText = categoryName.getText();
        // List<Category> categories = AccountWrapper.getUserCategories();
        // Category victimCategory = null;
        // for (Category category : categories) {
        //     if (category.getName().equals(categoryNameText)) {
        //         victimCategory = category;
        //         break;
        //     }
        // }
        // if (victimCategory!= null) {
        //     if (AccountWrapper.removeCategory(victimCategory)) {
        //         categoryName.clear();
        //         categoryDescription.clear();
        //         categoryNameError.setVisible(false);
        //         categoryDescriptionError.setVisible(false);
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