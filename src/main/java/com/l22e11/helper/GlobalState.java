package com.l22e11.helper;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import model.Charge;
import model.User;

public class GlobalState {
    public static ObservableList<Category> categoriesObservableList;
    public static ObservableList<Charge> expensesObservableList;
    public static MainTab currentTab;
	public static boolean settingsTabModified;
	public static List<SideTab> sideTabs;
    public static User user;
	public static boolean changesInCurrentCharge;
    public static Charge currentCharge;
	public static boolean changesInCurrentCategory;
    public static Category currentCategory;

	public static void initialize() {
		user = AccountWrapper.getAuthenticatedUser();
		categoriesObservableList = FXCollections.observableArrayList(AccountWrapper.getUserCategories());
		expensesObservableList = FXCollections.observableArrayList(AccountWrapper.getUserCharges());
		currentTab = MainTab.NONE;
		sideTabs = new ArrayList<>();
		changesInCurrentCharge = false;
		changesInCurrentCategory = false;
		settingsTabModified = false;
		currentCharge = null;
		currentCategory = null;
	}

	public static boolean isSideTabOpen(SideTab tab) {
		return sideTabs.contains(tab);
	}
}
