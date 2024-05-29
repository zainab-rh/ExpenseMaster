package com.l22e11.controllers;

import javafx.scene.input.MouseEvent;
import java.util.Map;
import static java.util.Map.entry;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.GlobalState;
import com.l22e11.helper.Utils;
import com.l22e11.helper.MainTab;
import com.l22e11.helper.SideTab;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

public class MainController implements Initializable {

    @FXML
    private AnchorPane sideBar;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label fullName, tabTitle, tabSubTitle;
	@FXML
	private Pane profilePicPaneCroppable;
	@FXML
	private HBox logOutArea, mainTab, sideTab;
	@FXML
	private VBox userArea, tabOptions;

	private static HBox staticMainTab;
	private static HBox staticSideTab;
	private static ImageView staticProfilePic;
	private static Pane staticProfilePicPaneCroppable;
	private static Label staticFullName;
	private static Label staticTabTitle, staticTabSubTitle;

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

		GlobalState.user = AccountWrapper.getAuthenticatedUser();

        GlobalState.reloadCategories();
		GlobalState.reloadExpenses();
		
		// Display name and profile picture in sidebar
        reloadTabBar();

		// Logout button
		logOutArea.setOnMouseClicked((event) -> {
			boolean isOk = AccountWrapper.logOutUser();
			if (isOk) App.showLandingStage();
		});

		userArea.setOnMouseClicked((event) -> {setMainTab(MainTab.SETTINGS);});

		tabList = tabOptions.getChildren();
		for (int idx = 0; idx < tabList.size(); ++idx) {
			final int i = idx;
			tabList.get(i).setOnMouseClicked((event) -> {setMainTab(TABS[i]);});
		}

		setMainTab(MainTab.EXPENSES);
		setSideTab(SideTab.MANAGE_CATEGORY);

		// TODO: Set search bar support
    }

	/*
	 * Correct way of reloading sideBar
	 */
	public static void reloadTabBar() {
		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			staticProfilePic.setImage(Utils.cropImage(GlobalState.user.getImage(), staticProfilePicPaneCroppable));
            staticFullName.setText(GlobalState.user.getName() + " " + GlobalState.user.getSurname());
		}), 50, TimeUnit.MILLISECONDS);
	}

	/*
	 * Correct way of reloading sideTab
	 */
	public static void setSideTab(SideTab selection) {
		if (selection == GlobalState.currentSideTab) return;
		GlobalState.currentSideTab = selection;

		String fxmlName = null;
		switch (selection) {
			case MANAGE_EXPENSE: fxmlName = "Expense"; break;
			case MANAGE_CATEGORY: fxmlName = "Category"; break;
			default: break;
		}

		staticSideTab.getChildren().clear();
		if (selection != SideTab.NONE) {
			Node tab = App.loadFXML(fxmlName);
			staticSideTab.getChildren().add(tab);

			VBox.setVgrow(tab, Priority.ALWAYS);
			HBox.setHgrow(tab, Priority.ALWAYS);
		}
	}

	/*
	 * Correct way of switching tabs
	 */
	public static void setMainTab(MainTab selection) {
		if (selection == GlobalState.currentTab) return;

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
        App.close();
    }
}
