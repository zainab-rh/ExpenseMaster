package com.l22e11.helper;

import java.text.Normalizer;

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
}
