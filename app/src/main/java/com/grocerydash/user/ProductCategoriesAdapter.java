package com.grocerydash.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductCategoriesAdapter extends RecyclerView.Adapter<ProductCategoriesAdapter.ViewHolder>{
    Context context;
    ArrayList<ProductCategories> productCategoryList;
    ProductCategories productCategories;

    public ProductCategoriesAdapter(Context context, ArrayList<ProductCategories> productCategories){
        this.context = context;
        this.productCategoryList = productCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_categories, parent, false);

        return new ProductCategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        productCategories = productCategoryList.get(position);
        holder.categoryName.setText(productCategories.getCategoryName());
        holder.categoryImage.setImageResource(productCategories.getCategoryImage());
    }

    @Override
    public int getItemCount(){
        return productCategoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.imageview_category_image);
            categoryName = itemView.findViewById(R.id.text_category_name);
        }
    }
}
