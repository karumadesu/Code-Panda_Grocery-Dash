package com.grocerydash.user;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

public class ProductDetailsFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layout;
    TextView textViewProductName, textViewProductPrice, productNamePath, productCategoryPath, productStock, textViewProductQuantity;
    ImageView productImage;
    ImageButton imageButtonSubtractQuantity, imageButtonAddQuantity;
    Button buttonAddToList;
    String productName, productPrice, productImageUrl;
    String[] productX, productY, productShelf;
    int productXLocation, productYLocation, productShelfNumber, productId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView = view.findViewById(R.id.recyclerView_recommendedProducts);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(((MainActivity)getActivity()).recommendedProductAdapter);

        textViewProductName = view.findViewById(R.id.textView_productDetailsName);
        textViewProductPrice = view.findViewById(R.id.textView_productDetailsPrice);
        productStock = view.findViewById(R.id.textView_productDetailsStock);
        productNamePath = view.findViewById(R.id.textView_productDetailsNamePath);
        productCategoryPath = view.findViewById(R.id.textView_productDetailsCategoryPath);
        productImage = view.findViewById(R.id.imageView_productDetailsImage);
        imageButtonSubtractQuantity = view.findViewById(R.id.imageButton_remove);
        imageButtonAddQuantity = view.findViewById(R.id.imageButton_add);
        textViewProductQuantity = view.findViewById(R.id.textView_productQuantity);
        buttonAddToList = view.findViewById(R.id.button_addToList);

        ((MainActivity) getActivity()).imageButtonBack.setVisibility(View.VISIBLE);

        imageButtonAddQuantity.setOnClickListener(v -> {
            ((MainActivity) getActivity()).productQuantity++;
            imageButtonAddQuantity.setColorFilter(getResources().getColor(R.color.orange));
            ((MainActivity) getActivity()).updateQuantity();
        });

        imageButtonSubtractQuantity.setOnClickListener(v -> {
            if (((MainActivity) getActivity()).productQuantity > 0) {
                ((MainActivity) getActivity()).productQuantity--;
                imageButtonSubtractQuantity.setColorFilter(getResources().getColor(R.color.orange));
                ((MainActivity) getActivity()).updateQuantity();
            }
        });

        buttonAddToList.setOnClickListener(v -> {
            if (((MainActivity) getActivity()).productQuantity > 0) {
                ((MainActivity) getActivity()).groceryList.add(new GroceryListClass(productName, productImageUrl,
                        productPrice, productShelfNumber, ((MainActivity) getActivity()).productQuantity,
                        productXLocation, productYLocation, productId, false));
            }
            exitFragment();

            Snackbar.make(getActivity().findViewById(R.id.coordinator_layout_main), ((MainActivity) getActivity()).productQuantity + "x " +
                    productName + " has been added to your list!", 750).show();
        });

        setUpProductDetails();
        displayRecommenededProducts();
    }

    public void setUpProductDetails() {
        for (ProductInformationClass i : ((MainActivity) getActivity()).productList) {
            if (i.getProductName().equals(((MainActivity) getActivity()).productName)) {
                productName = i.getProductName();
                productPrice = i.getProductPrice();
                productImageUrl = i.getProductImageUrl();
                productX = i.getProductLocationX().toArray(new String[0]);
                productY = i.getProductLocationY().toArray(new String[0]);
                productShelf = i.getProductShelfNumber().toArray(new String[0]);
                productXLocation = Integer.parseInt(productX[0]);
                productYLocation = Integer.parseInt(productY[0]);
                productShelfNumber = Integer.parseInt(productShelf[0]);
                productId = i.getProductId();

                for (Iterator<GroceryListClass> iterator = ((MainActivity) getActivity()).groceryList.iterator(); iterator.hasNext(); ) {
                    GroceryListClass j = iterator.next();

                    if (j.getProductName().equals(productName)) {
                        iterator.remove();
                        ((MainActivity) getActivity()).productQuantity = j.getProductQuantity();
                        ((MainActivity) getActivity()).groceryList.remove(j);
                    }
                }

                textViewProductName.setText(i.getProductName());
                textViewProductPrice.setText("₱" + i.getProductPrice());
                productCategoryPath.setText(i.getProductCategory());
                productCategoryPath.setPaintFlags(productCategoryPath.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                productNamePath.setText(i.getProductName());
                productNamePath.setPaintFlags(productNamePath.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                Picasso.get().load(String.valueOf(i.getProductImageUrl())).resize(360, 360).centerCrop().into(productImage);

                if (((MainActivity) getActivity()).productQuantity == 0) {
                    imageButtonSubtractQuantity.setColorFilter(getResources().getColor(R.color.light_gray));
                } else {
                    imageButtonSubtractQuantity.setColorFilter(getResources().getColor(R.color.icon));
                }

                if (i.getProductInStock() == 0) {
                    ((MainActivity) getActivity()).productQuantity = 0;

                    productStock.setText("Out of Stock");
                    productStock.setTextColor(getResources().getColor(R.color.red));
                    imageButtonAddQuantity.setColorFilter(getResources().getColor(R.color.light_gray));
                    imageButtonAddQuantity.setClickable(false);
                    imageButtonSubtractQuantity.setColorFilter(getResources().getColor(R.color.light_gray));
                    imageButtonSubtractQuantity.setClickable(false);
                } else {
                    productStock.setText("In Stock");
                    productStock.setTextColor(getResources().getColor(R.color.sub_text));
                }

                textViewProductQuantity.setText(String.valueOf(((MainActivity) getActivity()).productQuantity));
                if ((((MainActivity) getActivity()).productQuantity == 0 || i.getProductInStock() == 0)) {
                    buttonAddToList.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_gray)));
                    buttonAddToList.setText("Return to Home");
                }
            }
        }
    }

    public void exitFragment() {
        HomeFragment homeFragment = new HomeFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.frameLayout_withSearchView, homeFragment)
                .commit();
    }

    public void displayRecommenededProducts() {
        ((MainActivity)getActivity()).recommendedProductList.clear();
        CollectionReference recommendedProducts = db.collection("BranchName_Transactions").document(((MainActivity) getActivity()).productName)
                .collection("Frequently_Bought_Item");
        recommendedProducts.whereArrayContains("productRecommendedTo", productName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                ProductInformationClass info = d.toObject(ProductInformationClass.class);
                                ((MainActivity) getActivity()).recommendedProductList.add(info);
                            }
                            ((MainActivity) getActivity()).recommendedProductAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "No data found in Database", Toast.LENGTH_SHORT).show();
                            Log.d("CategoryList", "Error getting documents: ");
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // if we do not get any data or any error we are displaying
                        // a toast message that we do not get any data
                        Toast.makeText(getActivity(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}