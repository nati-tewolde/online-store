package com.pluralsight;

public class Product {
    // Apply static method possibly?Ad

    private String sku;
    private String name;
    private double price;
    private String department;

    public Product(String sku, String name, double price, String department) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.department = department;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDepartment() {
        return department;
    }
}
