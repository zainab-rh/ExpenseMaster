package com.l22e11.helper;

public class Expense {
    private String category;
    private double cost;
    private String date;
    private String title;
    private String description;
    private String invoiceImage;

    public Expense(String category, double cost, String date, String title, String description, String invoiceImage) {
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.title = title;
        this.description = description;
        this.invoiceImage = invoiceImage;
    }
    

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvoiceImage() {
        return invoiceImage;
    }

    public void setInvoiceImage(String invoiceImage) {
        this.invoiceImage = invoiceImage;
    }
}
