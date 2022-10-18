package com.grocerydash.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryInterface, ProductInterface, CategorizedProductInterface, FilteredProductInterface, GroceryListInterface{
    String searchString, productCategory, productName;
    int categoryNumber, productQuantity, checkPopular, currentlyAtCart;
    ArrayList<GroceryListClass> groceryList;
    ArrayList<ProductCategoryClass> productCategories;
    ArrayList<ProductInformationClass> productList, filteredProductList, popularProductList, categorizedProductList;
    ImageButton imageButtonHome, imageButtonCart, imageButtonBack;
    SearchView searchViewSearchProducts;
    PopularProductsAdapter popularProductsAdapter;
    ProductCategoriesAdapter productCategoriesAdapter;
    FilteredProductsAdapter filteredProductsAdapter;
    CategorizedProductsAdapter categorizedProductsAdapter;
    GroceryListAdapter groceryListAdapter;
    FragmentManager fragmentManager;
    FrameLayout layout;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Instances
        currentlyAtCart = 0;
        db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        productCategories = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        popularProductList = new ArrayList<>();
        categorizedProductList = new ArrayList<>();
        groceryList = new ArrayList<>();
        popularProductsAdapter = new PopularProductsAdapter(this, popularProductList, this);
        productCategoriesAdapter = new ProductCategoriesAdapter(this, productCategories, this);
        filteredProductsAdapter = new FilteredProductsAdapter(this, filteredProductList, this);
        categorizedProductsAdapter = new CategorizedProductsAdapter(this, categorizedProductList, this);
        groceryListAdapter = new GroceryListAdapter(this, groceryList, this);
        layout = findViewById(R.id.frameLayout_noSearchView);

        // Populate List Data
        setUpProductList();
        setUpPopularProductList();
        setUpProductCategoryList();

        // Start Home Fragment
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout_withSearchView, homeFragment)
                .commit();

        // Implement Search Field Listeners
        searchViewSearchProducts = findViewById(R.id.search_view_searchProducts);
        searchViewSearchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                searchString = newText;

                if(TextUtils.isEmpty(newText)){
                    filteredProductList.clear();

                    HomeFragment homeFragment = new HomeFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout_withSearchView, homeFragment)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
                else{
                    SearchFragment searchFragment = new SearchFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout_withSearchView, searchFragment)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }

                return true;
            }
        });

        // Implement Home Button Listeners
        imageButtonHome = findViewById(R.id.image_button_logo);
        imageButtonHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(currentlyAtCart == 1){
                    currentlyAtCart = 0;

                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                            .replace(R.id.frameLayout_noSearchView, new Fragment())
                            .commit();

                    HomeFragment homeFragment = new HomeFragment();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                            .replace(R.id.frameLayout_withSearchView, homeFragment)
                            .commit();
                }
                else if(currentlyAtCart == 2){
                    currentlyAtCart = 0;

                    HomeFragment homeFragment = new HomeFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                            .replace(R.id.frameLayout_withSearchView, homeFragment)
                            .commit();
                    closeKeyboard();
                    layout.setVisibility(View.GONE);
                    searchViewSearchProducts.setQuery("", false);
                    searchViewSearchProducts.clearFocus();
                }
            }
        });

        // Implement Cart Button Listeners
        imageButtonCart = findViewById(R.id.image_button_cart);
        imageButtonCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(currentlyAtCart != 1) {
                    currentlyAtCart = 1;

                    GroceryListFragment groceryListFragment = new GroceryListFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                            .replace(R.id.frameLayout_noSearchView, groceryListFragment)
                            .commit();
                    closeKeyboard();
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Implement Back Button Listeners
        imageButtonBack = findViewById(R.id.imageButton_back);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                closeKeyboard();
            }
        });
    }

    // Function to Retrieve All Products from Database
    private void setUpProductList(){
        CollectionReference productCollection = db.collection("BranchName_Products");
        productCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProductInformationClass info = d.   toObject(ProductInformationClass.class);
                            productList.add(info);
                        }
                    }
                    else{
                        Toast.makeText(this, "No products found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    // Function to Retrieve All Popular Products from Database
    private void setUpPopularProductList(){
        CollectionReference productCollection = db.collection("BranchName_Products");
        productCollection.whereEqualTo("productInStock", 1).whereEqualTo("productPopular", 1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProductInformationClass info = d.toObject(ProductInformationClass.class);
                            popularProductList.add(info);
                        }
                        popularProductsAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(this, "No products found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    // Function to Store All Category Information in an ArrayList
    private void setUpProductCategoryList(){
        String[] productCategoryNames = getResources().getStringArray(R.array.category_names);
        int[] productCategoryImages = {
                R.drawable.ic_meat,
                R.drawable.ic_poultry,
                R.drawable.ic_seafood,
                R.drawable.ic_fruits,
                R.drawable.ic_vegetables,
                R.drawable.ic_pantry,
                R.drawable.ic_snacks,
                R.drawable.ic_milk,
                R.drawable.ic_dairy,
                R.drawable.ic_deli,
                R.drawable.ic_beverage,
                R.drawable.ic_foodservicecenter,
                R.drawable.ic_cannedgoods,
                R.drawable.ic_frozengoods,
                R.drawable.ic_cookingessentials,
                R.drawable.ic_bakingneeds,
                R.drawable.ic_babyneeds,
                R.drawable.ic_personalcare,
                R.drawable.ic_home,
                R.drawable.ic_cleaningaids,
                R.drawable.ic_petitems
        };

        for(int i = 0; i < productCategoryNames.length; i++){
            productCategories.add(new ProductCategoryClass(productCategoryNames[i], productCategoryImages[i]));
        }
        productCategoriesAdapter.notifyDataSetChanged();
    }

    // Function to Close Device Keyboard
    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCategoryClick(int position) {
        currentlyAtCart = 2;

        productCategory = productCategories.get(position).getCategoryName();
        categoryNumber = position;

        CategorizedProductsFragment categorizedProductsFragment = new CategorizedProductsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frameLayout_withSearchView, categorizedProductsFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onProductClick(int position) {
        currentlyAtCart = 2;

        productName = popularProductList.get(position).getProductName();
        productQuantity = 1;

        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    // Function to Refresh Product Details Fragment
    public void updateQuantity(){
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .commit();
    }

    @Override
    public void onCategorizedProductClick(int position) {
        currentlyAtCart = 2;

        productName = categorizedProductList.get(position).getProductName();
        productQuantity = 1;

        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFilteredProductClick(int position) {
        currentlyAtCart = 2;

        productName = filteredProductList.get(position).getProductName();
        productQuantity = 1;

        closeKeyboard();
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onQuantityAdd(int position) {
        groceryList.get(position).productQuantity++;

        GroceryListFragment groceryListFragment = new GroceryListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout_noSearchView, groceryListFragment)
                .commit();
        closeKeyboard();
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQuantitySubtract(int position) {
        if(groceryList.get(position).productQuantity > 0){
            groceryList.get(position).productQuantity--;

            if(groceryList.get(position).productQuantity == 0){
                groceryList.remove(groceryList.get(position));
            }
        }

        GroceryListFragment groceryListFragment = new GroceryListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout_noSearchView, groceryListFragment)
                .commit();
        closeKeyboard();
        layout.setVisibility(View.VISIBLE);
    }
}