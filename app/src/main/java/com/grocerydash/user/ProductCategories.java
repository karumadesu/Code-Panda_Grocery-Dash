package com.grocerydash.user;

public class ProductCategories{
    String categoryName;
    int categoryImage;

    public ProductCategories(String categoryName, int categoryImage){
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public int getCategoryImage(){
        return categoryImage;
    }

}
