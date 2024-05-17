package com.l22e11.controllers;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML
    private Label nameError, surnameError, nickError, emailError, passwordError, passwordRepeatedError, logInError;

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
            logInError.setVisible(false);
            App.showMainStage();
        }else{
            logInError.setVisible(true);
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

        boolean[] exceptions = {true, true, true, true}; // name, surname, email, password
        checkRegister(name, surname, email, pass, passConfirmation, exceptions);
        boolean generalError = false;

        if(exceptions[0]) {nameError.setVisible(true); generalError = true;}
        if(exceptions[1]) {surnameError.setVisible(true); generalError = true;}
        if(exceptions[2]) {emailError.setVisible(true); generalError = true;}

        if(pass.length() == 0) {passwordError.setText("Password needed"); passwordError.setVisible(true); generalError = true;}
        else if (pass.length() < 8) {passwordError.setText("Password too short"); passwordError.setVisible(true); generalError = true;}
        else if(exceptions[3]) {passwordRepeatedError.setVisible(true); generalError = true;}
        
        if (!generalError){
            int isOk = AccountWrapper.registerUser(name, surname, email, nick, pass, profilePic, dateNow);
            if (isOk == 0) nickError.setVisible(true);
            if (isOk == 1) authenticationPane.getSelectionModel().selectFirst();
            // if (isOk == -1) DB error

            // Take out errors
            nameError.setVisible(false);
            surnameError.setVisible(false);
            emailError.setVisible(false);
            passwordError.setVisible(false);
            passwordRepeatedError.setVisible(false);

        }
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
    
    
    // CHECKING FUNCTIONALITIES FOR THE REGISTER
    @FXML
    private void checkRegister(String name, String surname, String email, String password, String password2, boolean[] exceptions){

        String regexPattern = "^[\\D]+";
        if (Pattern.compile(regexPattern).matcher(name).matches()){
            exceptions[0] = false;
        }
        if (Pattern.compile(regexPattern).matcher(surname).matches()){
            exceptions[1] = false;
        }
        regexPattern = "^(.+)@(\\S+)$";
        if (Pattern.compile(regexPattern).matcher(email).matches()){
            exceptions[2] = false;
        }
        if (password.equals(password2)) exceptions[3] = false;
    }
    
    
}
