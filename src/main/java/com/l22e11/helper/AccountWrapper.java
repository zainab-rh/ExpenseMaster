package com.l22e11.helper;

import java.time.LocalDate;
import java.util.List;

import javafx.scene.image.Image;
import model.Acount;
import model.Category;
import model.Charge;


public class AccountWrapper {
    
    // Wrapper call to the createUser from Acount API
    // Return values ->
    //  1 correct registration
    //  0 incorrect registration
    // -1 AcountDAO


    // Method to register a new user
    public int registerUser(String name, String surname, String email, String login, String password, Image image, LocalDate date) {
        try {
            boolean isOk = Acount.getInstance().registerUser(name, surname, email, login, password, image, date);
            if (isOk) return 1;
            else return 0;
        } catch (Exception AcountDAOException) {
            return -1;
        }
    }
 // Wrapper call to the LogIn method from the Acount API
    public boolean logInUserByCredentials(String
    login, String password)
    {
        try {
            boolean isOk = Acount.getInstance().logInUserByCredentials(login, password);
            return isOk;
        } catch (Exception AcountDAOException) {
            return false;
        }
    }
   
    // Wrapper call to the Logout method from the Acount API
    public boolean logOutUser() {
        try {
            boolean isOk = Acount.getInstance().logOutUser();
            return isOk;
        } catch (Exception AcountDAOException) {
            return false;
        }
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

    // Method to register a new charge
    public static boolean registerCharge(String name, String description, double cost, int units, Image scanImage, LocalDate date, Category category){
        try{
            boolean isOK = Acount.getInstance().registerCharge(name, description, cost, units, scanImage, date, category);
            return isOK;
        }catch (Exception AcountDAOException){ return false;}
    }   

  // Method to get the user's categories
    public List<Category> getUserCategories() {
        try {
            return Acount.getInstance().getUserCategories();
        } catch (Exception AcountDAOException) {
            return null;
        }
    }

    // Method to get the user's charges
    public List<Charge> getUserCharges() {
        try {
            return Acount.getInstance().getUserCharges();
        } catch (Exception AcountDAOException) {
            return null;
        }
    }
}