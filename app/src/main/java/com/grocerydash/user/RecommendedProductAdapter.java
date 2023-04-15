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

public class RecommendedProductAdapter extends RecyclerView.Adapter<RecommendedProductAdapter.ViewHolder>{
    RecommendedProductInterface recommendedProductInterface;
    Context context;
    ArrayList<ProductInformationClass> recommendedProductList;
    ProductInformationClass recommendedProductClass;

    public RecommendedProductAdapter(Context context, ArrayList<ProductInformationClass> recommendedProductList, RecommendedProductInterface recommendedProductInterface) {
        this.context = context;
        this.recommendedProductList = recommendedProductList;
        this.recommendedProductInterface = recommendedProductInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_recommended_products, parent, false);
        return new RecommendedProductAdapter.ViewHolder(view, recommendedProductInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        recommendedProductClass = recommendedProductList.get(position);
        holder.productName.setText(recommendedProductClass.getProductName());
        holder.productPrice.setText("₱" + String.format("%.2f", (Double.parseDouble(recommendedProductClass.getProductPrice()))));
        Picasso.get().load(String.valueOf(recommendedProductClass.getProductImageUrl() ))
                .resize(360, 360).centerCrop()
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return recommendedProductList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView, RecommendedProductInterface recommendedProductInterface) {

            super(itemView);
            productImage = itemView.findViewById(R.id.imageview_recommended_image);
            productName = itemView.findViewById(R.id.text_recommended_name);
            productPrice = itemView.findViewById(R.id.text_recommended_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recommendedProductInterface != null){
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION){
                            recommendedProductInterface.onRecommendedProductsClick(position);
                        }
                    }
                }
            });
        }
    }
}
