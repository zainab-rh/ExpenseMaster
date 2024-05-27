package com.l22e11.controllers;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.l22e11.helper.AccountWrapper;
import com.l22e11.helper.DataGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Category;
import model.Charge;
import model.User;
import javafx.scene.control.DatePicker;


public class DashboardController implements Initializable {
    @FXML
private BarChart<String, Double> monthlyExpensesChart;
@FXML
private LineChart<String, Double> yearlyExpensesChart;
@FXML
private PieChart categoryExpensesPieChart;
@FXML
private CategoryAxis monthlyExpensesCategoryAxis;
@FXML
private NumberAxis monthlyExpensesValueAxis;
@FXML
private NumberAxis yearlyExpensesValueAxis;
@FXML
private Label totalExpensesLabel;
@FXML
private ScrollPane categoriesExpensesScrollPane;
@FXML
private VBox categoriesExpensesContainer;
@FXML
private ComboBox<String> yearComboBox;
@FXML
private ComboBox<String> monthComboBox;
@FXML
private Button filterButton;
@FXML
private Button printReportButton;

@FXML
private DatePicker fromDatePicker;

@FXML
private DatePicker toDatePicker;

private BarChart<String, Double> comparisonChart;
private DataGenerator dataGenerator;
private User authenticatedUser;


    
@Override
public void initialize(URL arg0, ResourceBundle arg1) {
    authenticatedUser = AccountWrapper.getAuthenticatedUser();
    updateDashboardData();
    initializeComboBoxes();
    dataGenerator = new DataGenerator();
}


  private void initializeComboBoxes() {
        List<Integer> years = AccountWrapper.getUserCharges().stream()
               .map(charge -> charge.getDate().getYear())
               .distinct()
               .sorted()
               .collect(Collectors.toList());
        for (int year : years) {
            yearComboBox.getItems().add(String.valueOf(year));
        }
        monthComboBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    }

    private void createMonthlyExpensesChart(List<Charge> charges) {
        // Group charges by month and category
        Map<YearMonth, Map<Category, Double>> monthlyExpenses = new HashMap<>();
        for (Charge charge : charges) {
            YearMonth yearMonth = YearMonth.from(charge.getDate());
            Category category = charge.getCategory();
            double cost = charge.getCost();
            monthlyExpenses.computeIfAbsent(yearMonth, ym -> new HashMap<>())
                  .merge(category, cost, Double::sum);
        }

   // Populate the monthly expenses chart
        monthlyExpensesChart.getData().clear();
        for (Map.Entry<YearMonth, Map<Category, Double>> entry : monthlyExpenses.entrySet()) {
            YearMonth yearMonth = entry.getKey();
            XYChart.Series<String, Double> series = new XYChart.Series<>();
            series.setName(yearMonth.getMonth().name() + " " + yearMonth.getYear());
            for (Map.Entry<Category, Double> categoryEntry : entry.getValue().entrySet()) {
                series.getData().add(new XYChart.Data<>(categoryEntry.getKey().getName(), categoryEntry.getValue()));
            }
            monthlyExpensesChart.getData().add(series);
        }
}

private void createYearlyExpensesChart(List<Charge> charges) {
    // Group charges by year and category
    Map<Integer, Map<Category, Double>> yearlyExpenses = new HashMap<>();
    for (Charge charge : charges) {
        int year = charge.getDate().getYear();
        Category category = charge.getCategory();
        double cost = charge.getCost();
        yearlyExpenses.computeIfAbsent(year, y -> new HashMap<>())
              .merge(category, cost, Double::sum);
    }

    // Populate the yearly expenses chart
    yearlyExpensesChart.getData().clear();
    for (Map.Entry<Integer, Map<Category, Double>> entry : yearlyExpenses.entrySet()) {
        int year = entry.getKey();
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(String.valueOf(year));
        for (Map.Entry<Category, Double> categoryEntry : entry.getValue().entrySet()) {
            series.getData().add(new XYChart.Data<>(categoryEntry.getKey().getName(), categoryEntry.getValue()));
        }
        yearlyExpensesChart.getData().add(series);
    }
}

private void displayCategoriesExpenses(List<Category> categories, List<Charge> charges) {
    categoriesExpensesContainer.getChildren().clear();

    for (Category category : categories) {
        List<Charge> categoryCharges = charges.stream()
              .filter(charge -> charge.getCategory().equals(category))
              .collect(Collectors.toList());

        double categoryExpenses = categoryCharges.stream()
              .mapToDouble(Charge::getCost)
              .sum();

        Label categoryLabel = new Label(category.getName());
        Label categoryExpensesLabel = new Label(String.format("%.2f", categoryExpenses));
        categoriesExpensesContainer.getChildren().addAll(categoryLabel, categoryExpensesLabel);
    }
}
  

private void createComparisonChart(List<Charge> charges) {
    // Get the current month and year
    YearMonth currentYearMonth = YearMonth.now();

    // Get the expenses for the current month
    List<Charge> currentMonthCharges = charges.stream()
            .filter(charge -> YearMonth.from(charge.getDate()).equals(currentYearMonth))
            .collect(Collectors.toList());

    // Get the expenses for the previous month
    YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
    List<Charge> previousMonthCharges = charges.stream()
            .filter(charge -> YearMonth.from(charge.getDate()).equals(previousYearMonth))
            .collect(Collectors.toList());

    // Get the expenses for the same month in the previous year
    YearMonth sameMonthPreviousYear = currentYearMonth.minusYears(1);
    List<Charge> sameMonthPreviousYearCharges = charges.stream()
            .filter(charge -> YearMonth.from(charge.getDate()).equals(sameMonthPreviousYear))
            .collect(Collectors.toList());

    // Create the comparison chart
    comparisonChart.getData().clear();
    XYChart.Series<String, Double> currentMonthSeries = new XYChart.Series<>();
    currentMonthSeries.setName("Current Month");
    currentMonthSeries.getData().add(new XYChart.Data<>("Expenses", currentMonthCharges.stream().mapToDouble(Charge::getCost).sum()));

    XYChart.Series<String, Double> previousMonthSeries = new XYChart.Series<>();
    previousMonthSeries.setName("Previous Month");
    previousMonthSeries.getData().add(new XYChart.Data<>("Expenses", previousMonthCharges.stream().mapToDouble(Charge::getCost).sum()));

    XYChart.Series<String, Double> sameMonthPreviousYearSeries = new XYChart.Series<>();
    sameMonthPreviousYearSeries.setName("Same Month Previous Year");
    sameMonthPreviousYearSeries.getData().add(new XYChart.Data<>("Expenses", sameMonthPreviousYearCharges.stream().mapToDouble(Charge::getCost).sum()));

    comparisonChart.getData().addAll(List.of(currentMonthSeries, previousMonthSeries, sameMonthPreviousYearSeries));
}


@FXML
private void filterExpenses() {
    String selectedYear = yearComboBox.getSelectionModel().getSelectedItem();
    String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem();
    LocalDate fromDate = fromDatePicker.getValue();
    LocalDate toDate = toDatePicker.getValue();

    List<Charge> filteredCharges = AccountWrapper.getUserCharges().stream()
        .filter(charge -> {
            LocalDate chargeDate = charge.getDate();
            boolean matchesYear = (selectedYear == null) || (String.valueOf(chargeDate.getYear()).equals(selectedYear));
            boolean matchesMonth = (selectedMonth == null) || (chargeDate.getMonth().name().equalsIgnoreCase(selectedMonth));
            boolean matchesDateRange = (fromDate == null || !chargeDate.isBefore(fromDate)) &&
                                       (toDate == null || !chargeDate.isAfter(toDate));
            return matchesYear && matchesMonth && matchesDateRange;
        })
        .collect(Collectors.toList());

    updateDashboardData(filteredCharges);
}


private void updateDashboardData() {
    List<Charge> userCharges = AccountWrapper.getUserCharges();
    updateDashboardData(userCharges);
}

private void updateDashboardData(List<Charge> userCharges) {
    // Calculate total expenses
    double totalExpenses = userCharges.stream()
            .mapToDouble(Charge::getCost)
            .sum();
    totalExpensesLabel.setText(String.format("%.2f", totalExpenses));

    // Create monthly expenses chart
    createMonthlyExpensesChart(userCharges);

    // Create yearly expenses chart
    createYearlyExpensesChart(userCharges);

    // Display expenses by category
    displayCategoriesExpenses(AccountWrapper.getUserCategories(), userCharges);

    // Create comparison chart
    createComparisonChart(userCharges);
}



@FXML
private void printExpensesReport() {
    // Generate a PDF report with the user's expenses
    List<Charge> userCharges = AccountWrapper.getUserCharges();
    // Implement the logic to generate and print the PDF report
}
}

    





