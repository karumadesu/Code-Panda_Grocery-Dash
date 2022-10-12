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

public class FilteredProductsAdapter extends RecyclerView.Adapter<FilteredProductsAdapter.ViewHolder>{
    FilteredProductInterface filteredProductInterface;
    Context context;
    ArrayList<ProductInformationClass> productInformationClassList;
    ProductInformationClass productInformationClass;

    public FilteredProductsAdapter(Context context, ArrayList<ProductInformationClass> productInformationClassList, FilteredProductInterface filteredProductInterface){
        this.context = context;
        this.productInformationClassList = productInformationClassList;
        this.filteredProductInterface = filteredProductInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_search_products, parent, false);

        return new FilteredProductsAdapter.ViewHolder(view, filteredProductInterface);
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
        return productInformationClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView, FilteredProductInterface filteredProductInterface) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageview_search_product_image);
            productName = itemView.findViewById(R.id.text_search_product_name);
            productPrice = itemView.findViewById(R.id.text_search_product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        filteredProductInterface.onFilteredProductClick(position);
                    }
                }
            });
        }
    }

    public void setProductFilter(ArrayList<ProductInformationClass> searchFilter){
        this.productInformationClassList = searchFilter;
        notifyDataSetChanged();
    }
}
