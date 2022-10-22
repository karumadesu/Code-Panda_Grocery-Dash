package com.grocerydash.user;

public class GroceryListClass {
    String productName, productImageUrl, productPrice;
    int productQuantity, productX, productY;

    public GroceryListClass(String productName, String productImageUrl, String productPrice, int productQuantity, int productX, int productY) {
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productX = productX;
        this.productY = productY;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductX() {
        return productX;
    }

    public int getProductY() {
        return productY;
    }
}
