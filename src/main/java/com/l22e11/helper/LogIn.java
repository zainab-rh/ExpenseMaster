package com.l22e11.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.scene.image.Image;

import model.Acount;

public class LogIn {
    
    public static boolean logInUser ( String name, String password){
        boolean isOK = Acount.getInstance().logInUserByCredentials(name, password);
        return isOK;
    }

    // public static int createAccountUser (String name,
    // String surname, String email, String login,
    // String password, Image image, LocalDate date ) {
    //     
    // }
}
