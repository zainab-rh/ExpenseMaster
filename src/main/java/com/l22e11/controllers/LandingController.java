package com.l22e11.controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class LandingController implements Initializable {

    @FXML
    private TextField loginUser, registerName, registerSurname, registerEmail, registerNickname;
    @FXML
    private PasswordField registerPassConfirm, registerPass, loginPass;
    @FXML
    private Tab loginTab, registerTab;
    @FXML
    private TabPane authenticationPane;
    @FXML
    private Button registerBrowseProfilePic, loginSubmit, registerSubmit;
    @FXML
    private ImageView registerProfileView;

    // private int tabFocused = 0;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        double widthOfTab = authenticationPane.getWidth()/2;
    }

    @FXML
    private void onSubmitLogin(ActionEvent event) {
        String nick = loginUser.getText();
        String pass = loginPass.getText();
        boolean isOk = AccountWrapper.loginUser(nick, pass);
        
        if (isOk) {
            App.showMainStage();
        }
    }

    @FXML
    private void onSubmitRegister(ActionEvent event) {
        String name = registerName.getText();
        String surname = registerSurname.getText();
        String nick = registerNickname.getText();
        String email = registerEmail.getText();
        String pass = registerPass.getText();
        String passConfirmation = registerPassConfirm.getText();
        Image profilePic = registerProfileView.getImage();
        LocalDate dateNow = LocalDate.now();

        int isOk = AccountWrapper.registerUser(name, surname, email, nick, pass, profilePic, dateNow);
        System.out.println(isOk);
    }

    @FXML
    private void onBrowseProfilePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(App.getMainStage());
        Image image = new Image(selectedFile.toURI().toString());
        registerProfileView.setImage(image);
    }
}
