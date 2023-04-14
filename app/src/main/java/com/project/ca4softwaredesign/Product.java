package com.project.ca4softwaredesign;

import java.io.Serializable;

public class Product implements Serializable {
    String title;
    String category;
    String manufacturer;
    String price;

    public Product(String title, String category, String manufacturer, String price){
        this.title = title;
        this.category = category;
        this.manufacturer = manufacturer;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
