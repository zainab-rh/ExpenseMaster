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
    private Label nameError, surnameError, nickError, emailError, passwordError, passwordRepeatedError;

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
        }else{
            // USERNAME OR PASSWORD NOT FOUND
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

        boolean[] exceptions = {false, false, false, false};

        checkRegister(name, surname, email, pass, passConfirmation, exceptions);

        if(exceptions[0]) nameError.setText("The name is incorrect");
        if(exceptions[1]) surnameError.setText("The surname is incorrect");
        if(exceptions[2]) emailError.setText("The surname is incorrect");

        if (pass.length() < 8) passwordError.setText("Password too short");
        else if(exceptions[3]) passwordRepeatedError.setText("Password is repeated incorrectly");
        

        int isOk = AccountWrapper.registerUser(name, surname, email, nick, pass, profilePic, dateNow);
        if (isOk == 0) nickError.setText("Nickname already exists");
        if (isOk == 1) authenticationPane.getSelectionModel().selectFirst();
        // if (isOk == -1) DB error
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

        String regexPattern = "^[\\d]+";
        if (Pattern.compile(regexPattern).matcher(name).matches()){
            exceptions[0] = true;
        }
        if (Pattern.compile(regexPattern).matcher(surname).matches()){
            exceptions[1] = true;
        }
        regexPattern = "^(.+)@(\\S+)$";
        if (Pattern.compile(regexPattern).matcher(email).matches()){
            exceptions[2] = true;
        }
        if (!password.equals(password2)) exceptions[3] = true;
    }
    
    
}
