package com.grocerydash.user;

public class CategoryClass {
    String categoryName;
    int categoryImage;

    public CategoryClass(String categoryName, int categoryImage){
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
