package com.l22e11.helper;

import java.text.Normalizer;

public class Utils {

    /*
     * Remove accents and diacritics from text
     */
    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
    }
}
