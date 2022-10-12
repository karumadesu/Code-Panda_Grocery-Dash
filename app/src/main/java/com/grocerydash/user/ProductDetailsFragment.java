package com.grocerydash.user;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailsFragment extends Fragment{
    RecyclerView recyclerView;
    LinearLayoutManager layout;
    TextView productName, productPrice, productNamePath, productCategoryPath, productStock, productQuantity;
    ImageView productImage;
    ImageButton imageButtonSubtractQuantity, imageButtonAddQuantity;
    Button buttonAddToList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView = view.findViewById(R.id.recyclerView_recommendedProducts);
        recyclerView.setLayoutManager(layout);

        productName = view.findViewById(R.id.textView_productDetailsName);
        productPrice = view.findViewById(R.id.textView_productDetailsPrice);
        productStock = view.findViewById(R.id.textView_productDetailsStock);
        productNamePath = view.findViewById(R.id.textView_productDetailsNamePath);
        productCategoryPath = view.findViewById(R.id.textView_productDetailsCategoryPath);
        productImage = view.findViewById(R.id.imageView_productDetailsImage);
        imageButtonSubtractQuantity = view.findViewById(R.id.imageButton_remove);
        imageButtonAddQuantity = view.findViewById(R.id.imageButton_add);
        productQuantity = view.findViewById(R.id.textView_productQuantity);
        buttonAddToList = view.findViewById(R.id.button_addToList);

        imageButtonAddQuantity.setOnClickListener(v -> {
            ((MainActivity)getActivity()).productQuantity++;
            imageButtonAddQuantity.setColorFilter(getResources().getColor(R.color.orange));
            ((MainActivity)getActivity()).updateQuantity();
        });

        imageButtonSubtractQuantity.setOnClickListener(v -> {
            if(((MainActivity)getActivity()).productQuantity > 0){
                ((MainActivity)getActivity()).productQuantity--;
                imageButtonSubtractQuantity.setColorFilter(getResources().getColor(R.color.orange));
                ((MainActivity)getActivity()).updateQuantity();
            }
        });

        buttonAddToList.setOnClickListener(v -> {
            exitFragment();

        });

        setUpProductDetails();
    }

    public void setUpProductDetails(){
        for(ProductInformationClass i : ((MainActivity)getActivity()).productList){
            if(i.getProductName().equals(((MainActivity)getActivity()).productName)){
                productName.setText(i.getProductName());
                productPrice.setText("₱" + i.getProductPrice());
                productCategoryPath.setText(i.getProductCategory());
                productCategoryPath.setPaintFlags(productCategoryPath.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                productNamePath.setText(i.getProductName());
                productNamePath.setPaintFlags(productNamePath.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Picasso.get().load(String.valueOf(i.getProductImageUrl())).fit().into(productImage);

                if(i.getProductInStock() == 0){
                    productStock.setText("Out of Stock");
                    productStock.setTextColor(getResources().getColor(R.color.red));
                }
                else{
                    productStock.setText("In Stock");
                    productStock.setTextColor(getResources().getColor(R.color.orange));
                }

                productQuantity.setText(String.valueOf(((MainActivity)getActivity()).productQuantity));
                if((((MainActivity)getActivity()).productQuantity == 0 || i.getProductInStock() == 0)){
                    buttonAddToList.setClickable(false);
                    buttonAddToList.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                }
            }
        }
    }

    public void exitFragment(){
        HomeFragment homeFragment = new HomeFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .replace(R.id.frame_home_fragment, homeFragment)
                .commit();
    }
}