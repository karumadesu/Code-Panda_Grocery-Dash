package com.grocerydash.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class SearchFragment extends Fragment{
    private RecyclerView recyclerViewSearchProducts;
    private SearchProductsAdapter searchProductsAdapter;
    private LinearLayoutManager layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity)getActivity()).productSearch.clear();

        layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(RecyclerView.VERTICAL);

        searchProductsAdapter = new SearchProductsAdapter(getActivity(), ((MainActivity)getActivity()).productSearch);

        recyclerViewSearchProducts = view.findViewById(R.id.recyclerview_searchProducts);
        recyclerViewSearchProducts.setLayoutManager(layout);
        recyclerViewSearchProducts.setAdapter(searchProductsAdapter);

        setUpProductSearchModels();
    }

    private void setUpProductSearchModels(){
        for(ProductInformation i: ((MainActivity)getActivity()).productInformation){
            if(i.getProductName().toLowerCase().contains(((MainActivity)getActivity()).searchString.toLowerCase())){
                ((MainActivity)getActivity()).productSearch.add(i);
            }
        }

        if(((MainActivity) getActivity()).productSearch.isEmpty()){
            Toast.makeText(getActivity(), "No products found.", Toast.LENGTH_SHORT).show();
        }
        else{
            searchProductsAdapter.setProductFilter(((MainActivity) getActivity()).productSearch);
        }
    }
}