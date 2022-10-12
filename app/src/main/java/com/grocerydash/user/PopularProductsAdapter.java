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
    ProductInterface productInterface;
    Context context;
    ArrayList<ProductInformationClass> productInformationClassList;
    ProductInformationClass productInformationClass;

    public PopularProductsAdapter(Context context, ArrayList<ProductInformationClass> productInformationClassList, ProductInterface productInterface){
        this.context = context;
        this.productInformationClassList = productInformationClassList;
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_popular_product, parent, false);

        return new PopularProductsAdapter.ViewHolder(view, productInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        productInformationClass = productInformationClassList.get(position);
        holder.productName.setText(productInformationClass.getProductName());
        holder.productPrice.setText("₱" + productInformationClass.getProductPrice());
        Picasso.get().load(String.valueOf(productInformationClass.getProductImageUrl())).fit().into(holder.productImage);
    }

    @Override
    public int getItemCount(){
        return Math.min(productInformationClassList.size(), 4);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView, ProductInterface productInterface) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageview_popular_product_image);
            productName = itemView.findViewById(R.id.text_popular_product_name);
            productPrice = itemView.findViewById(R.id.text_popular_product_price);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(productInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            productInterface.onProductClick(position);
                        }
                    }
                }
            });
        }
    }
}
