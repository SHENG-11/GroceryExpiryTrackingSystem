package com.example.groceryexpirytrackingapp;

public class ItemVer1 {
    String name,exp_date,purchasedate,barcode,ImageUrl,key;
    int NumOfItem;

    public ItemVer1() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ItemVer1(String name, String exp_date, String barcode, String imageUrl, int numOfItem) {
        this.name = name;
        this.exp_date = exp_date;
        this.barcode = barcode;
        this.ImageUrl = imageUrl;
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

    public int getNumOfItem() {
        return NumOfItem;
    }

    public void setNumOfItem(int numOfItem) {
        NumOfItem = numOfItem;
    }
}
