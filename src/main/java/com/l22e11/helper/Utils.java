package com.l22e11.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

import javax.imageio.ImageIO;

import com.l22e11.App;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class Utils {

    /*
     * Remove accents and diacritics from text
     */
    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
    }

    /*
     * Make all first letters of a word uppercase
     */
    public static String capitalize(String text) {
        String[] words = text.split(" ");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedString
                    .append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
            }
        }

        return capitalizedString.toString().trim();
    }

    /*
     * Remove extra whitespaces
     */
    public static String removeExtraWhiteSpaces(String text) {
        while (text.contains("  ")) text = text.replaceAll("  ", " ");
        return text.strip();
    }

    /*
     * JavaFX Image to fit and round borders
     */
    public static Image cropImage(Image imageToCrop, Region nodeToClip) {
        int size = (int) Math.min(imageToCrop.getHeight(), imageToCrop.getWidth());
        int x = (int) (imageToCrop.getWidth() - size)/2;
        Image croppedImage = new WritableImage(imageToCrop.getPixelReader(), x, 0, size, size);

        if (croppedImage.getHeight() == 0.0) return null;

        size = (int) (nodeToClip.getWidth() / 2);
        Circle clip = new Circle(size, size, size-2);
        nodeToClip.setClip(clip);

        return croppedImage;
    }

	/*
	 * Get image from computer, crop it and display in specified ImageView
	 */
	public static Image loadNewProfilePictureInto(Region imagePane) {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(App.getMainStage());
        Image croppedImage = null;

        if (selectedFile != null) {
            try {
				BufferedImage bufferedImage = ImageIO.read(selectedFile);
            	croppedImage = Utils.cropImage(SwingFXUtils.toFXImage(bufferedImage, null), imagePane);
			} catch (IOException e) {}
        }
		return croppedImage;
	}
}
