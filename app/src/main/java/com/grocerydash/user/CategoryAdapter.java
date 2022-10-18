package com.grocerydash.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    CategoryInterface categoryInterface;
    Context context;
    ArrayList<CategoryClass> productCategoryList;
    CategoryClass categoryClass;

    public CategoryAdapter(Context context, ArrayList<CategoryClass> productCategories, CategoryInterface categoryInterface){
        this.context = context;
        this.productCategoryList = productCategories;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_product_categories, parent, false);

        return new CategoryAdapter.ViewHolder(view, categoryInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        categoryClass = productCategoryList.get(position);
        holder.categoryName.setText(categoryClass.getCategoryName());
        holder.categoryImage.setImageResource(categoryClass.getCategoryImage());
    }

    @Override
    public int getItemCount(){
        return productCategoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView, CategoryInterface categoryInterface) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.imageview_category_image);
            categoryName = itemView.findViewById(R.id.text_category_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(categoryInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            categoryInterface.onCategoryClick(position);
                        }
                    }
                }
            });
        }
    }
}
