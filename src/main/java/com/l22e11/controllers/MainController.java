package com.l22e11.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.User;

public class MainController implements Initializable {

    @FXML
    private AnchorPane sideBar;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label fullName;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        AccountWrapper.loginUser("ruben", "12345678");
        User user = AccountWrapper.getAuthenticatedUser();
        Platform.runLater(() -> {
            profilePic.setImage(user.getImage());
            fullName.setText(user.getName() + " " + user.getSurname());
        });
    }

    // @FXML
    // private void addCategory(ActionEvent event) {
    //     String category = newCategory.getText();
    //     String description = newDescription.getText();
    //     boolean isOk = AccountWrapper.registerCategory(category, description);
    //     if (isOk) {
    //         newCategory.setText("");
    //         newDescription.setText("");
    //         SuccessfullCategoryRegister.setText("Category added successfully");
    //         SuccessfullCategoryRegister.setVisible(true);
    //     }else{
    //         SuccessfullCategoryRegister.setText("Category already or incorrect");
    //         SuccessfullCategoryRegister.setVisible(true);
    //     }
    // }

    @FXML
    private void onAppMinimize(MouseEvent event) {
        App.minimize();
    }
    
    @FXML
    private void onAppClose(MouseEvent event) {
        App.close();
    }
}
