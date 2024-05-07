package com.l22e11.helper;

import java.time.LocalDate;
import javafx.scene.image.Image;
import model.Acount;

public class AccountWrapper {
    
    // Wrapper call to the createUser from Acount API
    // Return values ->
    //  1 correct registration
    //  0 incorrect registration
    // -1 AcountDAO
    public static int createAccountUser (String name,
    String surname, String email, String login,
    String password, Image image, LocalDate date ) {
        try{
            boolean isOk = Acount.getInstance().registerUser(name, surname, email, login, password, image, date);
            if (isOk) return 1;
            else return 0;
            
        }catch (Exception AcountDAOException){return -1;}
    }

    // Wrapper call to the LogIn from Acount API
    public static boolean logInUser ( String name, String password){
        try{
            boolean isOK = Acount.getInstance().logInUserByCredentials(name, password);

            return isOK;
        }catch (Exception AcountDAOException){ return false;}
    }

    // Wrapper call to the LogIn from Acount API
    public static boolean logOut (){
        try{
            boolean isOK = Acount.getInstance().logOutUser();
            return isOK;
        }catch (Exception AcountDAOException){ return false;}
    } 
    // Method to register a new category
    public static boolean registerCategory(String name, String description){
        try{
            boolean isOK = Acount.getInstance().registerCategory(name, description);
            return isOK;
        }catch (Exception AcountDAOException){ return false;}
    }

      // Method to remove a category
      public static boolean removeCategory(Category category) {
        try {
            boolean isOK = Acount.getInstance().removeCategory(category);
            return isOK;
        } catch (Exception AcountDAOException) {
            return false;
        }
}
