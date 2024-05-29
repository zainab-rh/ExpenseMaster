package com.l22e11.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import model.Charge;
import model.User;

public class GlobalState {
    public static ObservableList<Category> categoriesObservableList;
    public static ObservableList<Charge> expensesObservableList;
    public static MainTab currentTab = MainTab.NONE;
	public static SideTab currentSideTab = SideTab.NONE;
    public static User user;
    public static Charge currentCharge = null;
    public static Category currentCategory = null;

    public static void reloadCategories() {
        categoriesObservableList = FXCollections.observableList(AccountWrapper.getUserCategories());
    }

    public static void reloadExpenses() {
        expensesObservableList = FXCollections.observableList(AccountWrapper.getUserCharges());
    }
}
