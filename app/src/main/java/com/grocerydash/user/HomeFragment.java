package com.grocerydash.user;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        ((MainActivity)getActivity()).checkPopular = 0;
        ((MainActivity)getActivity()).imageButtonBack.setVisibility(View.INVISIBLE);

        layout1 = new GridLayoutManager(getActivity(), 2);
        layout2 = new GridLayoutManager(getActivity(), 3);

        nestedScrollView = view.findViewById(R.id.scrollview);
        nestedScrollView.scrollTo(0, 0);

        recyclerViewPopularProducts = view.findViewById(R.id.recyclerview_popularProducts);
        recyclerViewPopularProducts.setLayoutManager(layout1);
        recyclerViewPopularProducts.setAdapter(((MainActivity)getActivity()).popularProductsAdapter);

        recyclerViewProductCategories = view.findViewById(R.id.recyclerview_productCategory);
        recyclerViewProductCategories.setLayoutManager(layout2);
        recyclerViewProductCategories.setAdapter(((MainActivity)getActivity()).categoryAdapter);

        textSeeAllPopularProducts = view.findViewById(R.id.text_see_all_popular);
        textSeeAllPopularProducts.setOnClickListener(v -> {
            ((MainActivity)getActivity()).currentlyAtCart = 2;
            ((MainActivity)getActivity()).checkPopular = 1;

            CategorizedProductsFragment categorizedProductsFragment = new CategorizedProductsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.frameLayout_withSearchView, categorizedProductsFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }
}