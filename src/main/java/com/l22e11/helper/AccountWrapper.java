package com.l22e11.helper;

import java.time.LocalDate;
import java.util.List;
import com.l22e11.App;

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
		catch (Exception e) {return -1;}
        return (isOk ? 1 : 0);
    }

	/*
	 * Update user given current User object and parameters to update
	 */
	public static void updateUser(User user, String name, String surname, String email, String password, Image image) {
		user.setName(name);
		user.setSurname(surname);
		user.setEmail(email);
		user.setPassword(password);
		user.setImage(image);
	}

    /*
     * Wrapper call to the LogIn method from the Acount API
     */
    public static boolean loginUser(String nickName, String password) {
        try { return Acount.getInstance().logInUserByCredentials(nickName, password); }
		catch (Exception e) {return false;}
    }

    /*
     * Wrapper call to the Logout method from the Acount API
     */
    public static boolean logOutUser() {
        try { return Acount.getInstance().logOutUser(); }
		catch (Exception e) {return false;}
    }

    /*
     * Method to register a new category
     */
    public static boolean registerCategory(String name, String description) {
        try {
			boolean result = Acount.getInstance().registerCategory(name, description);
			if (result) {
				GlobalState.categoriesObservableList.clear();
				GlobalState.categoriesObservableList.addAll(getUserCategories());
			}
			return result;
		} catch (Exception e) {return false;}
    }

	/*
     * Method to update a new category
     */
    public static boolean updateCategory(String name, String description) {
		GlobalState.currentCategory.setName(name);
		GlobalState.currentCategory.setDescription(description);
		GlobalState.categoriesObservableList.add(null);
		GlobalState.categoriesObservableList.remove(null);
		return true;
    }

    /*
     * Method to remove a category
     */
    public static boolean removeCategory(Category category) {
        try {
			boolean result = Acount.getInstance().removeCategory(category);
			if (result) GlobalState.categoriesObservableList.remove(category);
			return result;
		} catch (Exception e) {return false;}
    }

    /*
     * Method to register a new charge
     */
    public static boolean registerCharge(String name, String description, double cost, int units, Image scanImage, LocalDate date, Category category) {
        try {
			boolean result = Acount.getInstance().registerCharge(name, description, cost, units, scanImage, date, category);
			if (result) {
				GlobalState.expensesObservableList.clear();
				GlobalState.expensesObservableList.addAll(getUserCharges());
			}
			return result;
		} catch (Exception e) {return false;}
    }

	/*
     * Method to update a new charge
     */
    public static boolean updateCharge(String name, String description, double cost, int units, Image scanImage, LocalDate date, Category category) {
		if (scanImage == null) scanImage = App.onePixelTransparent;
		
		GlobalState.currentCharge.setName(name);
		GlobalState.currentCharge.setDescription(description);
		GlobalState.currentCharge.setCost(cost);
		GlobalState.currentCharge.setUnits(units);
		GlobalState.currentCharge.setDate(date);
		GlobalState.currentCharge.setCategory(category);

		////// BUG: THE IMAGE IS NOT UPDATING, ERROR SEEMS TO BE FROM THE COMPILED JARS
		//          Image updates in DB, but not in Object
		// System.out.println("Before");
		// System.out.println(GlobalState.currentCharge.getImageScan().getWidth());
		GlobalState.currentCharge.setImageScan(scanImage);
		// System.out.println("After");
		// System.out.println(GlobalState.currentCharge.getImageScan().getWidth());
		///////

		GlobalState.expensesObservableList.clear();
		GlobalState.expensesObservableList.addAll(getUserCharges());

		return true;
    }

	/*
     * Method to remove a new charge
     */
    public static boolean removeCharge(Charge charge) {
        try {
			boolean result = Acount.getInstance().removeCharge(charge);
			if (result) GlobalState.expensesObservableList.remove(charge);
			return result;
		} catch (Exception e) {return false;}
    }

    /*
     * Method to get the user's categories
     */
    public static List<Category> getUserCategories() {
        try { return Acount.getInstance().getUserCategories(); }
		catch (Exception e) {return null;}
    }

    /*
     * Method to get the user's charges
     */
    public static List<Charge> getUserCharges() {
        try { return Acount.getInstance().getUserCharges(); }
		catch (Exception e) {return null;}
    }

    /*
     * Get currently logged in user
     */
    public static User getAuthenticatedUser() {
        try { return Acount.getInstance().getLoggedUser(); }
		catch (Exception e) {return null;}
    }
}