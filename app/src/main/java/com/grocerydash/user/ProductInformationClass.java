package com.grocerydash.user;

import java.util.ArrayList;

public class ProductInformationClass {
    int productId, productInStock, productPopular;
    String productCategory, productImageUrl, productName, productPrice;
    ArrayList<String> productLocationX, productLocationY, productShelfNumber;

    public ProductInformationClass(int productId, int productInStock, int productPopular, String productCategory,
                                   String productImageUrl, String productName, String productPrice,
                                   ArrayList<String> productLocationX, ArrayList<String> productLocationY,
                                   ArrayList<String> productShelfNumber) {
        this.productId = productId;
        this.productInStock = productInStock;
        this.productPopular = productPopular;
        this.productCategory = productCategory;
        this.productImageUrl = productImageUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productLocationX = productLocationX;
        this.productLocationY = productLocationY;
        this.productShelfNumber = productShelfNumber;
    }

    public ProductInformationClass() {
    }

    public int getProductId() {
        return productId;
    }

    public int getProductInStock() {
        return productInStock;
    }

    public int getProductPopular() {
        return productPopular;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public ArrayList<String> getProductLocationX() {
        return productLocationX;
    }

    public ArrayList<String> getProductLocationY() {
        return productLocationY;
    }

    public ArrayList<String> getProductShelfNumber() {
        return productShelfNumber;
    }
}
