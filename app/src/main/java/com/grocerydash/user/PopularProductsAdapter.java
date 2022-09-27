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

public class PopularProductsAdapter extends RecyclerView.Adapter<PopularProductsAdapter.ViewHolder>{
    Context context;
    ArrayList<ProductInformation> productInformationList;
    ProductInformation productInformation;

    public PopularProductsAdapter(Context context, ArrayList<ProductInformation> productInformationList){
        this.context = context;
        this.productInformationList = productInformationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popular_products, parent, false);

        return new PopularProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        productInformation = productInformationList.get(position);
        holder.productName.setText(productInformation.getProductName());
        holder.productPrice.setText(productInformation.getProductPrice());
        Picasso.get().load(String.valueOf(productInformation.getProductImageUrl())).fit().into(holder.productImage);
    }

    @Override
    public int getItemCount(){
        return productInformationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageview_popular_product_image);
            productName = itemView.findViewById(R.id.text_popular_product_name);
            productPrice = itemView.findViewById(R.id.text_popular_product_price);
        }
    }
}
