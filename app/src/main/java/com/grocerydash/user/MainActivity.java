package com.grocerydash.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private ImageButton imageButtonMenu, imageButtonCart;
    private RecyclerView recyclerViewPopularProducts, recyclerViewProductCategories;
    private PopularProductsAdapter popularProductsAdapter;
    private ProductCategoriesAdapter productCategoriesAdapter;
    private TextView textSeeAllPopularProducts, textSeeAllProductCategories;
    private LinearLayoutManager layout1, layout2;

    ArrayList<ProductInformation> productInformation;
    ArrayList<ProductCategories> productCategories;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productInformation = new ArrayList<>();
        productCategories = new ArrayList<>();

        layout1 = new LinearLayoutManager(this);
        layout1.setOrientation(RecyclerView.HORIZONTAL);

        layout2 = new LinearLayoutManager(this);
        layout2.setOrientation(RecyclerView.HORIZONTAL);

        popularProductsAdapter = new PopularProductsAdapter(this, productInformation);
        productCategoriesAdapter = new ProductCategoriesAdapter(this, productCategories);

        recyclerViewPopularProducts = findViewById(R.id.recyclerview_popularProducts);
        recyclerViewPopularProducts.setLayoutManager(layout1);
        recyclerViewPopularProducts.setAdapter(popularProductsAdapter);

        recyclerViewProductCategories = findViewById(R.id.recyclerview_productCategory);
        recyclerViewProductCategories.setLayoutManager(layout2);
        recyclerViewProductCategories.setAdapter(productCategoriesAdapter);

        setUpPopularProductModels();
        setUpProductCategoryModels();

        imageButtonMenu = findViewById(R.id.image_button_menu);
        imageButtonMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonCart = findViewById(R.id.image_button_cart);
        imageButtonCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        textSeeAllPopularProducts = findViewById(R.id.text_see_all_popular);
        textSeeAllPopularProducts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        textSeeAllProductCategories = findViewById(R.id.text_see_all_categories);
        textSeeAllPopularProducts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setUpPopularProductModels(){
        CollectionReference popularProducts = db.collection("BranchName_Products");
        popularProducts.whereEqualTo("productInStock", 1).get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list){
                        ProductInformation info = d.toObject(ProductInformation.class);
                        productInformation.add(info);
                    }
                    popularProductsAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this, "No products found.", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    private void setUpProductCategoryModels(){
        String[] productCategoryNames = getResources().getStringArray(R.array.category_names);

        for(int i = 0; i < productCategoryNames.length; i++){
            productCategories.add(new ProductCategories(productCategoryNames[i], R.drawable.baseline_add_circle_black_24));
        }
        productCategoriesAdapter.notifyDataSetChanged();
    }
}