package com.l22e11.helper;
import java.time.LocalDate;
import java.util.Random;
import model.Category;
import java.util.List;

public class DataGenerator {
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Emily", "William", "Olivia", "James", "Ava", "Robert", "Sophia"};
    private static final String[] LAST_NAMES = {"Doe", "Smith", "Johnson", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore"};
    private static final String[] CATEGORY_NAMES = {"Food", "Transportation", "Entertainment", "Utilities", "Shopping", "Bills"};
    private static final String[] CATEGORY_DESCRIPTIONS = {"Expenses for food", "Expenses for transportation", "Expenses for entertainment", "Expenses for utilities", "Expenses for shopping", "Expenses for bills"};

    public static void main(String[] args) {

        // Register users
        int numUsers = 5;
        for (int i = 0; i < numUsers; i++) {
            String firstName = getRandomFirstName();
            String lastName = getRandomLastName();
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";
            String username = firstName.toLowerCase() + lastName.toLowerCase();
            String password = "password123";

            int registrationStatus = AccountWrapper.registerUser(firstName, lastName, email, username, password, null, LocalDate.now());
            if (registrationStatus != 1) {
                System.out.println("User registration failed for: " + username);
                continue;
            }

            // Log in the user
            boolean loginStatus = AccountWrapper.loginUser(username, password);
            if (!loginStatus) {
                System.out.println("User login failed for: " + username);
                continue;
            }

            // Register categories
            for (int j = 0; j < CATEGORY_NAMES.length; j++) {
                boolean categoryRegistrationStatus = AccountWrapper.registerCategory(CATEGORY_NAMES[j], CATEGORY_DESCRIPTIONS[j]);
                if (!categoryRegistrationStatus) {
                    System.out.println("Category registration failed for: " + CATEGORY_NAMES[j]);
                }
            }

            // Register charges
            List<Category> categories = AccountWrapper.getUserCategories();
            for (Category category : categories) {
                int numCharges = 10;
                for (int k = 0; k < numCharges; k++) {
                    String chargeName = category.getName() + " Charge " + (k + 1);
                    String chargeDescription = "This is a charge for " + category.getName();
                    double cost = getRandomCost();
                    int units = getRandomUnits();
                    LocalDate date = getRandomDate();
                    boolean chargeRegistrationStatus = AccountWrapper.registerCharge(chargeName, chargeDescription, cost, units, null, date, category);
                    if (!chargeRegistrationStatus) {
                        System.out.println("Charge registration failed for: " + chargeName);
                    }
                }
            }
        }
    }

    private static String getRandomFirstName() {
        return FIRST_NAMES[new Random().nextInt(FIRST_NAMES.length)];
    }

    private static String getRandomLastName() {
        return LAST_NAMES[new Random().nextInt(LAST_NAMES.length)];
    }

    private static double getRandomCost() {
        return new Random().nextDouble() * 100;
    }

    private static int getRandomUnits() {
        return new Random().nextInt(10) + 1;
    }

    private static LocalDate getRandomDate() {
        return LocalDate.now().minusDays(new Random().nextInt(30));
    }
}