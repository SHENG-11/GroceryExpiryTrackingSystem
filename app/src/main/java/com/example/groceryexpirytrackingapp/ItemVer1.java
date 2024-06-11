package com.example.groceryexpirytrackingapp;

public class ItemVer1 {
    String name,exp_date,purchasedate,barcode,ImageUrl,Categories;
    int NumOfItem;

    public ItemVer1() {
    }

    public ItemVer1(String name, String exp_date, String purchasedate, String barcode, String imageUrl, String categories, int numOfItem) {
        this.name = name;
        this.exp_date = exp_date;
        this.purchasedate = purchasedate;
        this.barcode = barcode;
        this.ImageUrl = imageUrl;
        this.Categories = categories;
        this.NumOfItem = numOfItem;
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

    public String getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(String purchasedate) {
        this.purchasedate = purchasedate;
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
