package com.l22e11.helper;


import java.time.LocalDate;
import java.time.LocalTime;
// import account.*;
import javafx.scene.image.Image;



public class logIn {
    
    public static boolean logInUser ( String name, String password){
        boolean isOK = Account.getInstance().logInUserByCredentials(name, password);
        return isOk;
    }

    public static int createAccountUser (String name,
     String surname, String email, String login,
     String password, Image image, LocalDate date ){




    }
}
