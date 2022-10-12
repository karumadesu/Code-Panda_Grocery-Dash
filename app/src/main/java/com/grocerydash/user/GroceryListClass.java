package com.grocerydash.user;

public class GroceryListClass {
    String productName, productImageUrl, productPrice;
    int productQuantity;

    public GroceryListClass(String productName, String productImageUrl, String productPrice, int productQuantity) {
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
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
}
