package com.l22e11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

import com.l22e11.helper.AccountWrapper;

/*
 * Reminder for FXML app development:
 * 
 * - A "Stage" represents a window, as in, a draggable, closable window. You can load Scenes inside
 * - A "Scene" is what goes inside a window, you can load an FXML structure inside
 */

public class App extends Application {

    /* The main window */
    private static Stage mainStage;
    private static Scene scene;
	public static Image defaultImage;
	public static Image onePixelTransparent;

    /*
    * This function takes a Stage as an argument (aka the Window). It loads a scene inside a window
    */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/coins.png")));
		mainStage.initStyle(StageStyle.TRANSPARENT);
		defaultImage = new Image(getClass().getResourceAsStream("images/default-profile.png"));
		onePixelTransparent = new Image(getClass().getResourceAsStream("images/1pxtransparent.png"));
        loadFonts();
        loadStyles();

		final boolean START_IN_LOGIN = true;
		if (START_IN_LOGIN) {
            showLandingStage();
		} else {
			AccountWrapper.loginUser("pediro89", "12345678");
			showMainStage();
		}
    }

    /*
     * Landing page
     */
    public static void showLandingStage() {
        App.close();
        mainStage.setTitle("Expense Master - Login");
        scene = new Scene(loadFXML("Landing"), 1000, 1000);
        scene.setFill(Color.TRANSPARENT);
		mainStage.setMinHeight(1000);
		mainStage.setMinWidth(1000);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.sizeToScene();
        mainStage.show();
    }

    /*
     * Main page
     */
    public static void showMainStage() {
        App.close();
        mainStage.setTitle("Expense Master");
        scene = new Scene(loadFXML("Main"), 1500, 900);
        scene.setFill(Color.TRANSPARENT);
		mainStage.setMinWidth(500);
        mainStage.setMinHeight(450);
        mainStage.setWidth(1500);
        mainStage.setHeight(900);
        mainStage.setScene(scene);
        mainStage.setResizable(true);
        // mainStage.setMaximized(true);
        // mainStage.setFullScreen(true);
        mainStage.show();
    }

    /*
     * Used for changing screen visble on window
     * 
     * NOTICE THAT THIS FUNCTION IS STATIC!! In any part of the application you can do:
     *      App.setRoot("screenName");            // Font a = 

     * to change the scene of the window
     */
    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
    }

    /*
     * Used by this.setRoot() for loading fxml structure
     * If we want to read "primary.fxml", just call loadFXML("primary")s
     */
    public static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = getFxmlLoader(fxml);
        try { return fxmlLoader.load(); }
		catch (IOException e) {
            System.out.println("Error loading " + fxml + ".fxml");
            return null;
        }
    }

	public static FXMLLoader getFxmlLoader(String fxml) {
		String viewsDirectory = "views" + File.separator;
		return new FXMLLoader(App.class.getResource(viewsDirectory + fxml + ".fxml"));
	}

    /*
     * Return main window
     */
    public static Stage getMainStage() { return mainStage; }
        
    /*
     * Load styles globally
     */
    private void loadStyles() {
        Application.setUserAgentStylesheet(getClass().getResource("styles/global.css").toExternalForm());
    }

    /*
     * Load all fonts, since variable fonts are not supported
     */
    private void loadFonts() {
        // String fontStyles[] = {"Medium"};
        String fontStyles[] = {"Thin", "ExtraLight", "Light", "Regular", "Medium", "SemiBold", "Bold", "ExtraBold", "Black"};//, "ThinItalic", "ExtraLightItalic", "LightItalic", "Italic", "MediumItalic", "SemiBoldItalic", "BoldItalic", "ExtraBoldItalic", "BlackItalic"};
        for (int i = 0; i < fontStyles.length; ++i) {
			Font.loadFont(getClass().getResourceAsStream("fonts/Montserrat-" + fontStyles[i] + ".ttf"), 14);
        }
		Font.loadFont(getClass().getResourceAsStream("fonts/bootstrap-icons.ttf"), 26);
    }
    
    /*
     * Main function, leave as is
     */
    public static void main(String[] args) {
        launch();
    }

    /*
     * Close app
     */
    public static void close() {
        mainStage.close();
    }

    /*
     * Minimize app
     */
    public static void minimize() {
        mainStage.setIconified(true);
    }
}