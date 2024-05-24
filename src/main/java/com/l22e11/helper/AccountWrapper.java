package com.l22e11.helper;

import java.time.LocalDate;
import java.util.List;

import javafx.scene.image.Image;
import model.Acount;
import model.Category;
import model.Charge;
import model.User;

public class AccountWrapper {

    /*
     * Wrapper call to register user from Acount API
     *  
     * Returns:
     *   1 correct registration
     *   0 incorrect registration
     *  -1 AcountDAO
     */
    public static int registerUser(String name, String surname, String email, String nickName, String password, Image image, LocalDate date) {
        boolean isOk;
		try { isOk = Acount.getInstance().registerUser(name, surname, email, nickName, password, image, date); }
		catch (Exception AcountDAOException) {return -1;}
        return (isOk ? 1 : 0);
    }

    /*
     * Wrapper call to the LogIn method from the Acount API
     */
    public static boolean loginUser(String nickName, String password) {
        try { return Acount.getInstance().logInUserByCredentials(nickName, password); }
		catch (Exception AcountDAOException) {return false;}
    }

    /*
     * Wrapper call to the Logout method from the Acount API
     */
    public static boolean logOutUser() {
        try { return Acount.getInstance().logOutUser(); }
		catch (Exception AcountDAOException) {return false;}
    }

    /*
     * Method to register a new category
     */
    public static boolean registerCategory(String name, String description) {
        try { return Acount.getInstance().registerCategory(name, description); }
		catch (Exception AcountDAOException) {return false;}
    }

    /*
     * Method to remove a category
     */
    public static boolean removeCategory(Category category) {
        try { return Acount.getInstance().removeCategory(category); }
		catch (Exception AcountDAOException) {return false;}
    }

    /*
     * Method to register a new charge
     */
    public static boolean registerCharge(String name, String description, double cost, int units, Image scanImage, LocalDate date, Category category) {
        try { return Acount.getInstance().registerCharge(name, description, cost, units, scanImage, date, category); }
		catch (Exception AcountDAOException) {return false;}
    }

    /*
     * Method to get the user's categories
     */
    public static List<Category> getUserCategories() {
        try { return Acount.getInstance().getUserCategories(); }
		catch (Exception AcountDAOException) {return null;}
    }

    /*
     * Method to get the user's charges
     */
    public static List<Charge> getUserCharges() {
        try { return Acount.getInstance().getUserCharges(); }
		catch (Exception AcountDAOException) {return null;}
    }

    /*
     * Get currently logged in user
     */
    public static User getAuthenticatedUser() {
        try { return Acount.getInstance().getLoggedUser(); }
		catch (Exception AcountDAOException) {return null;}
    }
}