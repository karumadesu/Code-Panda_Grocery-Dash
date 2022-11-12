package com.grocerydash.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ListCompletedFragment extends Fragment {
    TextView totalPrice;
    Button returnToHome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String fetchedProductName, fetchedProductQuantity, frequentlyBoughtProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalPrice = view.findViewById(R.id.textView_price);
        totalPrice.setText("₱" + String.format("%.2f", ((MainActivity) getActivity()).totalPrice));

        returnToHome = view.findViewById(R.id.button_returnToHome);
        returnToHome.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                    .remove(this)
                    .commit();

            HomeFragment homeFragment = new HomeFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout_withSearchView, homeFragment)
                    .commit();

            ((MainActivity)getActivity()).currentlyAtCart = 0;
            ((MainActivity)getActivity()).groceryList.clear();
            ((MainActivity)getActivity()).totalPrice = 0;
            ((MainActivity)getActivity()).budget = 0;
            ((MainActivity)getActivity()).filterOn = false;
            ((MainActivity)getActivity()).setUpPopularProductList();
            ((MainActivity)getActivity()).setUpProductCategoryList();
        });
        createTransactionDatabase();
    }

    public void createTransactionDatabase() {

        for (GroceryListClass i : ((MainActivity) getActivity()).groceryList) {
            for (GroceryListClass j : ((MainActivity) getActivity()).groceryList) {
                if (!i.getProductName().equals(j.getProductName())) {
                    String productName = j.getProductName();
                    String documentName = i.getProductName();
                    String productPrice = j.getProductPrice();
                    String productImage = j.getProductImageUrl();
                    int documentId = i.getProductId();

                    Map<String, Object> addProducts = new HashMap<>();
                    addProducts.put("productName", productName);
                    addProducts.put("productRecommendedTo", FieldValue.arrayUnion(documentName));
                    addProducts.put("productPrice", productPrice);
                    addProducts.put("productImageUrl", productImage);

                    Map<String, Object> product = new HashMap<>();
                    product.put("productId", documentId);

                    db.collection("BranchName_Transactions")
                            .document(documentName)
                            .set(product);
                    db.collection("BranchName_Transactions")
                            .document(documentName)
                            .collection("Frequently_Bought_Item")
                            .document(productName)
                            .set(addProducts, SetOptions.merge());

                    db.collection("BranchName_Transactions")
                            .document(documentName)
                            .collection("Frequently_Bought_Item")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.contains("productQuantity")) {
                                            String getProductName = document.getString("productName");
                                            String getProductQuantity = document.getString("productQuantity");
                                            fetchedProductName = getProductName;
                                            fetchedProductQuantity = getProductQuantity;

                                            if (fetchedProductName.equals(productName)) {
                                                int updatedProductQuantity = Integer.parseInt(fetchedProductQuantity);

                                                Map<String, Object> updateProduct = new HashMap<>();
                                                updateProduct.put("productQuantity", String.valueOf(updatedProductQuantity + 1));

                                                db.collection("BranchName_Transactions")
                                                        .document(documentName)
                                                        .collection("Frequently_Bought_Item")
                                                        .document(productName)
                                                        .update(updateProduct);

                                                Log.d("update", String.valueOf(updatedProductQuantity));
                                            }
                                        } else {

                                            Map<String, Object> addProducts1 = new HashMap<>();
                                            addProducts1.put("productName", productName);
                                            addProducts1.put("productQuantity", "1");
                                            addProducts1.put("productRecommendedTo", FieldValue.arrayUnion(documentName));
                                            addProducts1.put("productPrice", productPrice);
                                            addProducts1.put("productImageUrl", productImage);

                                            db.collection("BranchName_Transactions")
                                                    .document(documentName)
                                                    .collection("Frequently_Bought_Item")
                                                    .document(productName)
                                                    .set(addProducts1, SetOptions.merge());
                                        }

                                        Log.d("products", fetchedProductName + " " + fetchedProductQuantity);
                                    }
                                }
                            });
                }
            }
        }
    }
}