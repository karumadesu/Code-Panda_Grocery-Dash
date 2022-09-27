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
    private RecyclerView recyclerViewPopularProducts;
    private PopularProductsAdapter popularProductsAdapter;
    private TextView textSeeAllPopularProducts;
    private LinearLayoutManager layout;

    ArrayList<ProductInformation> productInformation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productInformation = new ArrayList<>();

        layout = new LinearLayoutManager(this);
        layout.setOrientation(RecyclerView.HORIZONTAL);

        popularProductsAdapter = new PopularProductsAdapter(this, productInformation);

        recyclerViewPopularProducts = findViewById(R.id.recyclerview_popularProducts);
        recyclerViewPopularProducts.setLayoutManager(layout);
        recyclerViewPopularProducts.setAdapter(popularProductsAdapter);

        setUpPopularProductModels();

        imageButtonMenu = findViewById(R.id.image_button_menu);
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageButtonCart = findViewById(R.id.image_button_cart);
        imageButtonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textSeeAllPopularProducts = findViewById(R.id.text_see_all_popular);
        textSeeAllPopularProducts.setOnClickListener(new View.OnClickListener() {
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
}