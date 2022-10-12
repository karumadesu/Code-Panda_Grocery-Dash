package com.grocerydash.user;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class HomeFragment extends Fragment{
    private TextView textSeeAllPopularProducts;
    private RecyclerView recyclerViewPopularProducts, recyclerViewProductCategories;
    private GridLayoutManager layout1, layout2;
    private NestedScrollView nestedScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        layout1 = new GridLayoutManager(getActivity(), 2);
        layout2 = new GridLayoutManager(getActivity(), 3);

        nestedScrollView = view.findViewById(R.id.scrollview);
        nestedScrollView.scrollTo(0, 0);

        recyclerViewPopularProducts = view.findViewById(R.id.recyclerview_popularProducts);
        recyclerViewPopularProducts.setLayoutManager(layout1);
        recyclerViewPopularProducts.setAdapter(((MainActivity)getActivity()).popularProductsAdapter);

        recyclerViewProductCategories = view.findViewById(R.id.recyclerview_productCategory);
        recyclerViewProductCategories.setLayoutManager(layout2);
        recyclerViewProductCategories.setAdapter(((MainActivity)getActivity()).productCategoriesAdapter);
    }
}