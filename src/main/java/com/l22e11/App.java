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

    /*
    * This function takes a Stage as an argument (aka the Window). It loads a scene inside a window
    */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/coins.png")));
		mainStage.initStyle(StageStyle.TRANSPARENT);
        loadFonts();
        loadStyles();
		// Start in login:
        showLandingStage();

		// Start in main:
        AccountWrapper.loginUser("pediro89", "123456789"); showMainStage();
    }

    /*
     * Landing page
     */
    public static void showLandingStage() {
        App.close();
        scene = new Scene(loadFXML("Landing"), 1000, 1000);
        mainStage.setTitle("Expense Master - Login");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.sizeToScene();
        scene.setFill(Color.TRANSPARENT);
        mainStage.show();
    }

    /*
     * Main page
     */
    public static void showMainStage() {
        App.close();
        scene = new Scene(loadFXML("Main"), 1920, 1080);
        mainStage.setTitle("Expense Master");
        mainStage.setScene(scene);
        mainStage.setResizable(true);
        mainStage.setMaximized(true);
        // mainStage.setFullScreen(true);
        scene.setFill(Color.TRANSPARENT);
        mainStage.show();
    }

    /*
     * Used for changing screen visble on window
     * 
     * NOTICE THAT THIS FUNCTION IS STATIC!! In any part of the application you can do:
     *      App.setRoot("screenName");
     * to change the scene of the window
     */
    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
    }

    /*
     * Used by this.setRoot() for loading fxml structure
     * If we want to read "primary.fxml", just call loadFXML("primary")s
     */
    private static Parent loadFXML(String fxml) {
        String viewsDirectory = "views" + File.separator;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(viewsDirectory + fxml + ".fxml"));
        try { return fxmlLoader.load(); }
		catch (IOException e) {
            System.out.println("Error loading " + fxml + ".fxml");
            return null;
        }
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
            Font a = Font.loadFont(getClass().getResourceAsStream("fonts/Montserrat-" + fontStyles[i] + ".ttf"), 14);
            System.out.println(a.getName() + "   " + a.getFamily());
        }
        Font a = Font.loadFont(getClass().getResourceAsStream("fonts/bootstrap-icons.ttf"), 26);
        System.out.println(a.getName() + "   " + a.getFamily());
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