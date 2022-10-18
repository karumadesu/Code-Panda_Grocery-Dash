package com.grocerydash.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.annotation.Nullable;

public class SearchFragment extends Fragment{
    TextView textViewNoProducts;
    ImageView easterEgg;
    RecyclerView recyclerViewSearchProducts;
    LinearLayoutManager layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).filteredProductList.clear();
        ((MainActivity)getActivity()).imageButtonBack.setVisibility(View.INVISIBLE);

        layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(RecyclerView.VERTICAL);

        recyclerViewSearchProducts = view.findViewById(R.id.recyclerview_searchProducts);
        recyclerViewSearchProducts.setLayoutManager(layout);
        recyclerViewSearchProducts.setAdapter(((MainActivity)getActivity()).filteredProductsAdapter);

        textViewNoProducts = view.findViewById(R.id.text_no_product_found);
        easterEgg = view.findViewById(R.id.easter_egg);

        setUpFilteredList();
    }

    private void setUpFilteredList(){
        for(ProductInformationClass i: ((MainActivity)getActivity()).productList){
            if(((MainActivity)getActivity()).searchString.toLowerCase().equals("matlab")){
                recyclerViewSearchProducts.setVisibility(View.GONE);
                easterEgg.setVisibility(View.VISIBLE);
            }
            else if(i.getProductName().toLowerCase().contains(((MainActivity)getActivity()).searchString.toLowerCase())){
                ((MainActivity)getActivity()).filteredProductList.add(i);
            }
        }

        if(((MainActivity)getActivity()).filteredProductList.isEmpty()){
            String[] kaomojis = getResources().getStringArray(R.array.kaomojis);

            textViewNoProducts.setText(kaomojis[(int)(Math.random() * 7)] + "\n\nNo products found with '" + ((MainActivity)getActivity()).searchString + "'");
            textViewNoProducts.setVisibility(View.VISIBLE);
            recyclerViewSearchProducts.setVisibility(View.GONE);
        }
        else{
            ((MainActivity)getActivity()).filteredProductsAdapter.setProductFilter(((MainActivity)getActivity()).filteredProductList);
        }
    }
}