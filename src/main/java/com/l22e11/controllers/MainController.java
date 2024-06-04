package com.l22e11.controllers;

import javafx.scene.input.MouseEvent;

import java.util.Map;
import static java.util.Map.entry;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.Animations;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.Utils;
import com.l22e11.helper.MainTab;
import com.l22e11.helper.SideTab;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

public class MainController implements Initializable {

    @FXML
    private ScrollPane sideBar, mainScrollPane, sideTabScrollPane;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label fullName, tabTitle, tabSubTitle;
	@FXML
	private Pane profilePicPaneCroppable;
	@FXML
	private HBox logOutArea, mainTab, sideTabBackButton;
	@FXML
	private StackPane sideTab;
	@FXML
	private VBox userArea, tabOptions, sideTabPane;

	private static HBox staticMainTab;
	private static StackPane staticSideTab;
	private static ImageView staticProfilePic;
	private static Pane staticProfilePicPaneCroppable;
	private static Label staticFullName;
	private static Label staticTabTitle, staticTabSubTitle;
	private static VBox staticSideTabPane;

	private static ObservableList<Node> tabList;
	private static final MainTab TABS[] = {MainTab.DASHBOARD, MainTab.EXPENSES, MainTab.CATEGORIES};
	private static final Map<MainTab, Integer> TABS_MAP = Map.ofEntries(
		entry(MainTab.NONE, 0),
		entry(TABS[0], 0),
		entry(TABS[1], 1),
		entry(TABS[2], 2)
	);

	@Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		staticMainTab = mainTab;
		staticSideTab = sideTab;
		staticProfilePic = profilePic;
		staticFullName = fullName;
		staticProfilePicPaneCroppable = profilePicPaneCroppable;
		staticTabTitle = tabTitle;
		staticTabSubTitle = tabSubTitle;
		staticSideTabPane = sideTabPane;

		Animations.animateScroll(sideBar);
		Animations.animateScroll(mainScrollPane);
		Animations.animateScroll(sideTabScrollPane);

		GlobalState.initialize();
		
		// Display name and profile picture in sidebar
        reloadTabBar();

		// Logout button
		logOutArea.setOnMouseClicked((event) -> {
			if (closeAllTabs() == false) return;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Logging out");
			alert.setHeaderText("Logging out");
			alert.setContentText("Are you sure you want to log out?");
			if (alert.showAndWait().get() == ButtonType.OK) {
				if (AccountWrapper.logOutUser()) App.showLandingStage();
			}
		});

		userArea.setOnMouseClicked((event) -> {setMainTab(MainTab.SETTINGS);});

		tabList = tabOptions.getChildren();
		for (int idx = 0; idx < tabList.size(); ++idx) {
			final int i = idx;
			tabList.get(i).setOnMouseClicked((event) -> {setMainTab(TABS[i]);});
		}

		setMainTab(MainTab.EXPENSES);
		staticSideTabPane.setVisible(false);
		staticSideTabPane.setManaged(false);

		sideTabBackButton.setOnMouseClicked((event) -> {
			closeSideTab();
		});

		// TODO: Set search bar support
    }

	/*
	 * Correct way of reloading sideBar
	 */
	public static void reloadTabBar() {
		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			staticProfilePic.setImage(Utils.cropImage(GlobalState.user.getImage(), staticProfilePicPaneCroppable, Integer.MAX_VALUE));
            staticFullName.setText(GlobalState.user.getName() + " " + GlobalState.user.getSurname());
		}), 50, TimeUnit.MILLISECONDS);
	}

	/*
	 * Open a side tab
	 */
	public static boolean openSideTab(SideTab selection) {
		if ((selection == SideTab.CREATE_EXPENSE || selection == SideTab.UPDATE_EXPENSE) && GlobalState.isSideTabOpen(selection) && ExpenseController.requestDiscardChanges() == false) return false;
		if ((selection == SideTab.CREATE_CATEGORY || selection == SideTab.UPDATE_CATEGORY) && GlobalState.isSideTabOpen(selection) && CategoryController.requestDiscardChanges() == false) return false;

		String fxmlName = null;
		switch (selection) {
			case CREATE_EXPENSE: case UPDATE_EXPENSE: fxmlName = "Expense"; break;
			case CREATE_CATEGORY: case UPDATE_CATEGORY: fxmlName = "Category"; break;
			default: break;
		}

		int idxTop = GlobalState.sideTabs.size()-1;
		if (idxTop >= 0) {
			staticSideTab.getChildren().get(idxTop).setVisible(false);
			staticSideTab.getChildren().get(idxTop).setManaged(false);
		} else {
			staticSideTabPane.setVisible(true);
			staticSideTabPane.setManaged(true);
		}

		Node tab = App.loadFXML(fxmlName);
		GlobalState.sideTabs.add(selection);
		staticSideTab.getChildren().add(tab);

		VBox.setVgrow(tab, Priority.ALWAYS);
		HBox.setHgrow(tab, Priority.ALWAYS);

		return true;
	}
	
	/*
	 * Close a side tab
	 */
	public static boolean closeSideTab() {
		int idxTop = GlobalState.sideTabs.size()-1;
		if ((GlobalState.sideTabs.get(idxTop) == SideTab.CREATE_EXPENSE || GlobalState.sideTabs.get(idxTop) == SideTab.UPDATE_EXPENSE) && ExpenseController.requestDiscardChanges() == false) return false;
		if ((GlobalState.sideTabs.get(idxTop) == SideTab.CREATE_CATEGORY || GlobalState.sideTabs.get(idxTop) == SideTab.UPDATE_CATEGORY) && CategoryController.requestDiscardChanges() == false) return false;

		staticSideTab.getChildren().remove(idxTop);
		GlobalState.sideTabs.remove(idxTop);
		idxTop--;
		if (idxTop >= 0) {
			staticSideTab.getChildren().get(idxTop).setVisible(true);
			staticSideTab.getChildren().get(idxTop).setManaged(true);
		} else {
			staticSideTabPane.setVisible(false);
			staticSideTabPane.setManaged(false);
		}

		return true;
	}
	
	/*
	 * Close all side tabs
	 */
	public static boolean closeAllTabs() {
		int tabsToPop = GlobalState.sideTabs.size();
		while (tabsToPop > 0) {
			if (closeSideTab() == false) return false;
			--tabsToPop;
		}
		return true;
	}

	/*
	 * Correct way of switching tabs
	 */
	public static void setMainTab(MainTab selection) {
		if (selection == GlobalState.currentTab) return;
		if (closeAllTabs() == false) return;
		if (GlobalState.settingsTabModified && GlobalState.currentTab == MainTab.SETTINGS && SettingsController.requestDiscardChanges() == false) return;

		if (GlobalState.currentTab != MainTab.SETTINGS) tabList.get(TABS_MAP.get(GlobalState.currentTab)).getStyleClass().remove("selectedSideBarItem");
		if (selection != MainTab.SETTINGS) tabList.get(TABS_MAP.get(selection)).getStyleClass().add("selectedSideBarItem");
		GlobalState.currentTab = selection;

		String fxmlName = null, title = null, subTitle = null;
		switch (selection) {
			case DASHBOARD:
				fxmlName = "Dashboard";
				title = "Dashboard";
				subTitle = "Explore your Account";
				break;
			case CATEGORIES:
				fxmlName = "Categories";
				title = "Categories";
				subTitle = "Manage Categories";
				break;
			case EXPENSES:
				fxmlName = "Expenses";
				title = "Expenses";
				subTitle = "Manage Expenses";
				break;
			case SETTINGS:
				fxmlName = "Settings";
				title = "Settings";
				subTitle = "Account Settings";
				break;
			default: break;
		}

		staticTabTitle.setText(title);
		staticTabSubTitle.setText(subTitle);
		Node tab = App.loadFXML(fxmlName);
		staticMainTab.getChildren().clear();
		staticMainTab.getChildren().add(tab);

		VBox.setVgrow(tab, Priority.ALWAYS);
        HBox.setHgrow(tab, Priority.ALWAYS);
	}

    @FXML
    private void onAppMinimize(MouseEvent event) {
        App.minimize();
    }
    
    @FXML
    private void onAppClose(MouseEvent event) {
		if (closeAllTabs() == false) return;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Leaving ExpenseMaster");
		alert.setHeaderText("Leaving ExpenseMaster");
		alert.setContentText("Are you sure you want to quit ExpenseMaster?");
		if (alert.showAndWait().get() == ButtonType.OK) {
	        App.close();
		}
    }
}
