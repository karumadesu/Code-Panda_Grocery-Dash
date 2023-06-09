package com.grocerydash.user;

public class GroceryListClass {
    String productName, productImageUrl, productPrice;
    int productQuantity, productX, productY, shelfNumber, productId;
    boolean isVisited;

    public GroceryListClass(String productName, String productImageUrl, String productPrice, int shelfNumber, int productQuantity, int productX, int productY, int productId,boolean isVisited) {
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productX = productX;
        this.productY = productY;
        this.isVisited = isVisited;
        this.productId = productId;
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

    public int getShelfNumber() {
        return shelfNumber;
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

    public int getProductId() { return productId; }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
