package com.l22e11.helper;

import java.text.Normalizer;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

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

        if (croppedImage.getHeight() == 0.0) {
            return null;
        }

        size = (int) (nodeToClip.getWidth() / 2);
        System.out.println(size);
        Circle clip = new Circle(size, size, size-2);
        nodeToClip.setClip(clip);

        return croppedImage;
    }
}
