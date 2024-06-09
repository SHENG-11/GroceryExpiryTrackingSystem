package com.example.groceryexpirytrackingapp;

public class ItemVer1 {
    String name,exp_date,remainDay,barcode,ImageUrl,Categories;
    int NumOfItem;

    public ItemVer1() {
    }

    public ItemVer1(String name, String exp_date, String remainDay, String barcode, String imageUrl, String categories, int numOfItem) {
        this.name = name;
        this.exp_date = exp_date;
        this.remainDay = remainDay;
        this.barcode = barcode;
        ImageUrl = imageUrl;
        Categories = categories;
        NumOfItem = numOfItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getRemainDay() {
        return remainDay;
    }

    public void setRemainDay(String remainDay) {
        this.remainDay = remainDay;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public int getNumOfItem() {
        return NumOfItem;
    }

    public void setNumOfItem(int numOfItem) {
        NumOfItem = numOfItem;
    }
}
