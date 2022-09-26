package com.grocerydash.user;

public class PopularProducts {
    String productName, productPrice, productImage;

    public PopularProducts(String productName, String productPrice, String productImage){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return productImage;
    }
}
