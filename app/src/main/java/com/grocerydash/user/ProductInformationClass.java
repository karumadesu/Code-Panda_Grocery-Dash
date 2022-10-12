package com.grocerydash.user;

public class ProductInformationClass {
    int productId, productInStock, productLocationX, productLocationY, productPopular;
    String productCategory, productImageUrl, productName, productPrice, productShelfNumber;

    public ProductInformationClass(){
    }

    public ProductInformationClass(String productCategory, int productId, String productImageUrl, int productInStock,
                                   String productName, String productPrice,
                                   String productShelfNumber, int productAisleNumber){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.productCategory = productCategory;
        this.productId = productId;
        this.productInStock = productInStock;
        this.productShelfNumber = productShelfNumber;
        this.productPopular = productAisleNumber;
    }

    public String getProductCategory(){
        return productCategory;
    }

    public String getProductImageUrl(){
        return productImageUrl;
    }

    public String getProductName(){
        return productName;
    }

    public String getProductPrice(){
        return productPrice;
    }

    public String getProductShelfNumber(){
        return productShelfNumber;
    }

    public int getProductId(){
        return productId;
    }

    public int getProductInStock(){
        return productInStock;
    }

    public int getProductPopular(){
        return productPopular;
    }
}
