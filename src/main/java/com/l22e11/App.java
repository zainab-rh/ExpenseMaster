package com.l22e11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

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
    public void start(Stage stage) throws IOException {
        loadFonts();
        
        scene = new Scene(loadFXML("Landing"), 640, 480);
        stage.setScene(scene);
        // scene.setFill(Color.TRANSPARENT);
        // stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
        mainStage = stage;
    }

    /*
     * Used for changing screen visble on window
     * 
     * NOTICE THAT THIS FUNCTION IS STATIC!! In any part of the application you can do:
     *      App.setRoot("screenName");
     * to change the scene of the window
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /*
     * Used by this.setRoot() for loading fxml structure
     * If we want to read "primary.fxml", just call loadFXML("primary")s
     */
    private static Parent loadFXML(String fxml) throws IOException {
        String viewsDirectory = "views" + File.separator;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(viewsDirectory + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /*
     * Return main window
     */
    public static Stage getMainStage() { return mainStage; }
        
    /*
    * Load all fonts, since variable fonts are not supported
     */
    private void loadFonts() {
        String fontStyles[] = {"Thin", "ExtraLight", "Light", "Regular", "Medium", "SemiBold", "Bold", "ExtraBold", "Black", "ThinItalic", "ExtraLightItalic", "LightItalic", "Italic", "MediumItalic", "SemiBoldItalic", "BoldItalic", "ExtraBoldItalic", "BlackItalic"};
        for (int i = 0; i < fontStyles.length; ++i) {
            Font a = Font.loadFont(getClass().getResourceAsStream("fonts/Montserrat-" + fontStyles[i] + ".ttf"), 12);
            System.out.println(a.getName() + "   " + a.getFamily());
        }
    }
    
    /*
     * Main function, leave as is
     */
    public static void main(String[] args) {
        launch();
    }

}