package com.grocerydash.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
    private TextView textSeeAllPopularProducts, textSeeAllProductCategories;
    private RecyclerView recyclerViewPopularProducts, recyclerViewProductCategories;
    private PopularProductsAdapter popularProductsAdapter;
    private ProductCategoriesAdapter productCategoriesAdapter;
    private LinearLayoutManager layout1, layout2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        layout1 = new LinearLayoutManager(getActivity());
        layout1.setOrientation(RecyclerView.HORIZONTAL);

        layout2 = new LinearLayoutManager(getActivity());
        layout2.setOrientation(RecyclerView.HORIZONTAL);

        popularProductsAdapter = new PopularProductsAdapter(getActivity(), ((MainActivity)getActivity()).productInformation);
        productCategoriesAdapter = new ProductCategoriesAdapter(getActivity(), ((MainActivity)getActivity()).productCategories);

        recyclerViewPopularProducts = view.findViewById(R.id.recyclerview_popularProducts);
        recyclerViewPopularProducts.setLayoutManager(layout1);
        recyclerViewPopularProducts.setAdapter(popularProductsAdapter);

        recyclerViewProductCategories = view.findViewById(R.id.recyclerview_productCategory);
        recyclerViewProductCategories.setLayoutManager(layout2);
        recyclerViewProductCategories.setAdapter(productCategoriesAdapter);

        setUpPopularProductModels();
        setUpProductCategoryModels();

        textSeeAllPopularProducts = view.findViewById(R.id.text_see_all_popular);
        textSeeAllPopularProducts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        textSeeAllProductCategories = view.findViewById(R.id.text_see_all_categories);
        textSeeAllProductCategories.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }

    private void setUpPopularProductModels(){
        CollectionReference popularProducts = ((MainActivity)getActivity()).db.collection("BranchName_Products");
        popularProducts.whereEqualTo("productInStock", 1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProductInformation info = d.toObject(ProductInformation.class);
                            ((MainActivity)getActivity()).productInformation.add(info);
                        }
                        popularProductsAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(getActivity(), "No products found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    private void setUpProductCategoryModels(){
        String[] productCategoryNames = getResources().getStringArray(R.array.category_names);

        for(int i = 0; i < productCategoryNames.length; i++){
            ((MainActivity)getActivity()).productCategories.add(new ProductCategories(productCategoryNames[i], R.drawable.baseline_add_circle_black_24));
        }
        productCategoriesAdapter.notifyDataSetChanged();
    }
}