package com.l22e11.controllers;

import javafx.scene.input.MouseEvent;
import java.util.Map;
import static java.util.Map.entry;

import com.l22e11.App;
import com.l22e11.helper.AccountWrapper;
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
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import model.User;

public class MainController implements Initializable {

    @FXML
    private AnchorPane sideBar;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label fullName;
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

	private static MainTab currentTab = MainTab.NONE;
	private static SideTab currentSideTab = SideTab.NONE;

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
		
		// Display name and profile picture in sidebar
        reloadSideBar();

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

		setMainTab(MainTab.DASHBOARD);

    }

	/*
	 * Correct way of reloading sideBar
	 */
	public static void reloadSideBar() {
		User user = AccountWrapper.getAuthenticatedUser();

		Executors.newScheduledThreadPool(1).schedule(() -> Platform.runLater(() -> {
			staticProfilePic.setImage(Utils.cropImage(user.getImage(), staticProfilePicPaneCroppable));
            staticFullName.setText(user.getName() + " " + user.getSurname());
		}), 50, TimeUnit.MILLISECONDS);
	}

	/*
	 * Correct way of reloading sideTab
	 */
	public static void setSideTab(SideTab selection) {
		if (selection == currentSideTab) return;
		currentSideTab = selection;

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
		if (selection == currentTab) return;

		if (currentTab != MainTab.SETTINGS) tabList.get(TABS_MAP.get(currentTab)).getStyleClass().remove("selectedSideBarItem");
		if (selection != MainTab.SETTINGS) tabList.get(TABS_MAP.get(selection)).getStyleClass().add("selectedSideBarItem");
		currentTab = selection;

		String fxmlName = null;
		switch (selection) {
			case DASHBOARD: fxmlName = "Dashboard"; break;
			case CATEGORIES: fxmlName = "Categories"; break;
			case EXPENSES: fxmlName = "Expenses"; break;
			case SETTINGS: fxmlName = "Settings"; break;
			default: break;
		}

		staticMainTab.getChildren().clear();
		Node tab = App.loadFXML(fxmlName);
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
