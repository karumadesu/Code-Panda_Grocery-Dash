package com.grocerydash.user;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategorizedProductsFragment extends Fragment{
    RecyclerView recyclerView;
    GridLayoutManager layout;
    TextView noProductsText, categoryTitle, categoryDescription, categoryPath;
    String[] descriptionContents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_categorized_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).categorizedProductList.clear();

        layout = new GridLayoutManager(getActivity(), 2);
        layout.setOrientation(RecyclerView.VERTICAL);

        recyclerView = view.findViewById(R.id.recyclerView_categorizedProducts);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(((MainActivity)getActivity()).categorizedProductsAdapter);

        noProductsText = view.findViewById(R.id.text_no_product_found);
        categoryTitle = view.findViewById(R.id.textView_categorizedTitle);
        categoryDescription = view.findViewById(R.id.textView_categoryDescription);
        categoryPath = view.findViewById(R.id.text_category_path);
        descriptionContents = getResources().getStringArray(R.array.category_descriptions);

        setUpCategorizedList();
    }

    private void setUpCategorizedList(){
        for(ProductInformationClass i: ((MainActivity)getActivity()).productList){
            if(((MainActivity)getActivity()).checkPopular == 1){
                if(i.getProductPopular() == (((MainActivity)getActivity()).checkPopular)){
                    ((MainActivity)getActivity()).categorizedProductList.add(i);
                }
            }
            else if(i.getProductCategory().equals(((MainActivity)getActivity()).productCategory)){
                ((MainActivity)getActivity()).categorizedProductList.add(i);
            }
        }

        if(((MainActivity)getActivity()).checkPopular == 1){
            categoryTitle.setText(getResources().getString(R.string.title_popular_products));
            categoryDescription.setText(getResources().getString(R.string.description_popular_products));
            categoryPath.setText(getResources().getString(R.string.title_popular_products));
            categoryPath.setPaintFlags(categoryPath.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        else{
            categoryTitle.setText((((MainActivity)getActivity()).productCategory));
            categoryDescription.setText(descriptionContents[((MainActivity)getActivity()).categoryNumber]);
            categoryPath.setText((((MainActivity)getActivity()).productCategory));
            categoryPath.setPaintFlags(categoryPath.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        if(((MainActivity)getActivity()).categorizedProductList.isEmpty()){
            noProductsText.setText("\n\n¯\\_(ツ)_/¯\n\nNo products found under '" + ((MainActivity)getActivity()).productCategory + "'");
            noProductsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            ((MainActivity)getActivity()).categorizedProductsAdapter.setCategoryFilter(((MainActivity)getActivity()).categorizedProductList);
        }
    }
}