package com.l22e11.helper;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.l22e11.App;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Category;

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
     * JavaFX crop Image to fit inside rounded rectangle borders, use radius = Integer.MAX_VALUE for a circle
     */
    public static Image cropImage(Image imageToCrop, Region nodeToClip, double radius) {
        if (imageToCrop == null) return imageToCrop;
		double imageWHRatio = imageToCrop.getWidth() / imageToCrop.getHeight();
		double regionWHRatio = nodeToClip.getWidth() / nodeToClip.getHeight();
		double x, y, w, h;
		if (imageWHRatio > regionWHRatio) {
			h = imageToCrop.getHeight();
			w = h / (nodeToClip.getHeight() / nodeToClip.getWidth());
			x = (imageToCrop.getWidth() - w) / 2.0;
			y = 0;
		} else {
			w = imageToCrop.getWidth();
			h = w / (nodeToClip.getWidth() / nodeToClip.getHeight());
			x = 0;
			y = (imageToCrop.getHeight() - h) / 2.0;
		}

		Image croppedImage = new WritableImage(imageToCrop.getPixelReader(), (int)x, (int)y, (int)w, (int)h);
		if (croppedImage.getHeight() == 0.0) return null;

		Rectangle clip = new Rectangle(2, 2, nodeToClip.getWidth()-4, nodeToClip.getHeight()-4);
		clip.setArcHeight(radius);
		clip.setArcWidth(radius);
        nodeToClip.setClip(clip);

        return croppedImage;
    }

	/*
	 * Get image from computer, crop it and display in specified ImageView
	 */
	public static Image loadNewPictureInto(Region imagePane, int radius) {
		return Utils.cropImage(loadNewPicture(), imagePane, radius);
	}

	public static Image loadNewPicture() {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(App.getMainStage());
        Image uncroppedImage = null;

        if (selectedFile != null) {
            try {
				uncroppedImage = SwingFXUtils.toFXImage(ImageIO.read(selectedFile), null);
			} catch (IOException e) {}
        }
		return uncroppedImage;
	}

    public static Category getCategoryByName(String name) {
        for (Category cat : GlobalState.categoriesObservableList) {
            if (cat.getName().substring(8).equals(name)) return cat;
        }
        return null;
    }

	public static String localDateToString(LocalDate date) {
		return date.getDayOfMonth() + " " + Utils.capitalize(date.getMonth().toString()) + " " + date.getYear();
	}

	public static String toPrice(double d) {
		return String.format(Locale.US, "%.2f", Math.round(d * 100)/100.0);
	}

	public static double getBrightness(Color color) {
		return 0.2126*color.getRed() + 0.7152*color.getGreen() + 0.0722*color.getBlue();
	}
}
