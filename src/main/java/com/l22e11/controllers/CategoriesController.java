package com.l22e11.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.SideTab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Category;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    private ListView<Category> categoriesListView;


	public static ObservableList<Category> categories;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        List<Category> list = new ArrayList<Category>();
        categories = FXCollections.observableList(list);

        categoriesListView.setItems(categories);
	}

    private Category findCategory(String name){

        String categoryNameText = categoryName.getText();
        for (Category category : categories) {
            if (category.getName().equals(categoryNameText)) {
                return category;
            }
        }
        return null;
    }

    @FXML 
    private void onModifyCategory(ActionEvent event){

        String categoryNameText = categoryName.getText();
        Category victimCategory = findCategory(categoryNameText);

        CategoryController.currentCategory = victimCategory;
        MainController.setSideTab(SideTab.MANAGE_CATEGORY);

    }

    @FXML 
    private void onAddCategory(ActionEvent event){

        String categoryNameText = categoryName.getText();
        Category victimCategory = findCategory(categoryNameText);

        CategoryController.currentCategory = victimCategory;
        MainController.setSideTab(SideTab.MANAGE_CATEGORY);

    }
    @FXML //TODO
    private void onRemoveCategory(MouseEvent event) {


        String categoryNameText = categoryName.getText();
        Category victimCategory = findCategory(categoryNameText);

        System.out.println(victimCategory.getName());
        if (victimCategory!= null) {
            if (AccountWrapper.removeCategory(victimCategory)) {
                categoryName.clear();
                categoryNameError.setVisible(false);

            } else {
                categoryNameError.setText("Error removing category");
                categoryNameError.setVisible(true);
            }
        } else {
            categoryNameError.setText("Category not found");
            categoryNameError.setVisible(true);
        }
    }
}