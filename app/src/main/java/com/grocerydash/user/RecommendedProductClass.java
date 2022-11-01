package com.grocerydash.user;

public class RecommendedProductClass {
    String productPrice, productQuantity, productName, productImageUrl;
    int productId;

    public RecommendedProductClass() {

    }

    public RecommendedProductClass(String productPrice, String productQuantity, String productName, String productImageUrl, int productId) {
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productName = productName;
        this.productId = productId;
        this.productImageUrl = productImageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl(){ return productImageUrl; }

    public int getProductId() {
        return productId;
    }
}