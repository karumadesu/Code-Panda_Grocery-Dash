package com.grocerydash.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.ViewHolder>{
    Context context;
    ArrayList<GroceryListClass> groceryListClassList;
    GroceryListClass groceryListClass;
    GroceryListInterface groceryListInterface;

    public GroceryListAdapter(Context context, ArrayList<GroceryListClass> groceryListClassList, GroceryListInterface groceryListInterface){
        this.context = context;
        this.groceryListClassList = groceryListClassList;
        this.groceryListInterface = groceryListInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.model_grocery_list, parent, false);

        return new GroceryListAdapter.ViewHolder(view, groceryListInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        groceryListClass = groceryListClassList.get(position);
        holder.groceryListClass = groceryListClass;
        holder.productName.setText(groceryListClass.getProductName());
        holder.productPrice.setText("₱" + String.format("%.2f", (Double.parseDouble(groceryListClass.getProductPrice()) * Double.valueOf(groceryListClass.getProductQuantity()))));
        holder.productQuantity.setText(String.valueOf(groceryListClass.getProductQuantity()));
        Picasso.get().load(String.valueOf(groceryListClass.getProductImageUrl())).fit().into(holder.productImage);
    }

    @Override
    public int getItemCount(){
        return groceryListClassList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productPrice, productQuantity;
        ImageButton addQuantity, subtractQuantity;
        GroceryListClass groceryListClass;

        public ViewHolder(@NonNull View itemView, GroceryListInterface groceryListInterface) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imageView_groceryListProductImage);
            productName = itemView.findViewById(R.id.textView_groceryListProductName);
            productPrice = itemView.findViewById(R.id.textView_groceryListProductPrice);
            productQuantity = itemView.findViewById(R.id.textView_groceryListProductQuantity);
            addQuantity = itemView.findViewById(R.id.imageButton_groceryListAddQuantity);
            subtractQuantity = itemView.findViewById(R.id.imageButton_groceryListSubtractQuantity);

            int orange = itemView.getResources().getColor(R.color.orange);

            addQuantity.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(groceryListInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            addQuantity.setColorFilter(orange);
                            groceryListInterface.onQuantityAdd(position);
                        }
                    }
                }
            });

            subtractQuantity.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(groceryListClass.productQuantity > 0){
                        if(groceryListInterface != null){
                            int position = getAdapterPosition();

                            if(position != RecyclerView.NO_POSITION){
                                subtractQuantity.setColorFilter(orange);
                                groceryListInterface.onQuantitySubtract(position);
                            }
                        }
                    }
                }
            });
        }
    }
}
