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

public class CategorizedProductsAdapter extends RecyclerView.Adapter<CategorizedProductsAdapter.ViewHolder>{
    CategorizedProductInterface categorizedProductInterface;
    Context context;
    ArrayList<ProductInformationClass> productInformationClassList;
    ProductInformationClass productInformationClass;

    public CategorizedProductsAdapter(Context context, ArrayList<ProductInformationClass> productInformationClassList, CategorizedProductInterface categorizedProductInterface){
        this.context = context;
        this.productInformationClassList = productInformationClassList;
        this.categorizedProductInterface = categorizedProductInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_categorized_products, parent, false);

        return new CategorizedProductsAdapter.ViewHolder(view, categorizedProductInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        productInformationClass = productInformationClassList.get(position);
        holder.productName.setText(productInformationClass.getProductName());
        holder.productPrice.setText("₱" + productInformationClass.getProductPrice());
        Picasso.get().load(String.valueOf(productInformationClass.getProductImageUrl())).fit().into(holder.productImage);

        if(productInformationClass.getProductPopular() == 1){
            holder.popularProductTag.setVisibility(View.VISIBLE);
            holder.popularProductText.setVisibility(View.VISIBLE);
            holder.popularProductIcon.setVisibility(View.VISIBLE);
        }
        else{
            holder.popularProductTag.setVisibility(View.INVISIBLE);
            holder.popularProductText.setVisibility(View.INVISIBLE);
            holder.popularProductIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount(){
        return productInformationClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage, popularProductTag, popularProductIcon;
        TextView productName, productPrice, popularProductText;

        public ViewHolder(@NonNull View itemView, CategorizedProductInterface categorizedProductInterface) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageview_popular_product_image);
            productName = itemView.findViewById(R.id.text_popular_product_name);
            productPrice = itemView.findViewById(R.id.text_popular_product_price);
            popularProductTag = itemView.findViewById(R.id.imageview_popular_product_tag);
            popularProductText = itemView.findViewById(R.id.text_popular_product);
            popularProductIcon = itemView.findViewById(R.id.imageview_popular_product_icon);

            itemView.setOnClickListener(v -> {
                if(categorizedProductInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        categorizedProductInterface.onCategorizedProductClick(position);
                    }
                }
            });
        }
    }

    public void setCategoryFilter(ArrayList<ProductInformationClass> categoryFilter){
        this.productInformationClassList = categoryFilter;
    }
}
