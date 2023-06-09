package com.grocerydash.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryInterface, PopularProductsInterface, CategorizedProductInterface, FilteredProductInterface, GroceryListInterface, RecommendedProductInterface{
    int categoryNumber, productQuantity, checkPopular, currentlyAtCart, numberOfColumns, numberOfRows, itemCount;
    double totalPrice, budget;
    boolean filterOn;
    String[] mapLayout, searchHints;
    String searchString, productCategory, productName;
    ArrayList<GroceryListClass> groceryList;
    ArrayList<CategoryClass> productCategoryList;
    ArrayList<ProductInformationClass> productList, filteredProductList, popularProductList, categorizedProductList, recommendedProductList;
    ArrayList<StoreLayoutClass> storeLayoutList;
    ArrayList<ArrayList<StoreLayoutClass>> graphEdges, graphNodes;
    ImageButton imageButtonHome, imageButtonCart, imageButtonBack, imageButtonFilter;
    SearchView searchViewSearchProducts;
    PopularProductsAdapter popularProductsAdapter;
    CategoryAdapter categoryAdapter;
    FilteredProductsAdapter filteredProductsAdapter;
    CategorizedProductsAdapter categorizedProductsAdapter;
    RecommendedProductAdapter recommendedProductAdapter;
    GroceryListAdapter groceryListAdapter;
    StoreLayoutAdapter storeLayoutAdapter;
    FragmentManager fragmentManager;
    FrameLayout layout;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Instances
        budget = 0;
        itemCount = 0;
        totalPrice = 0;
        currentlyAtCart = 0;
        numberOfColumns = 57;
        numberOfRows = 79;
        filterOn = false;

        db = FirebaseFirestore.getInstance();
        searchHints = getResources().getStringArray(R.array.searchHints);
        layout = findViewById(R.id.frameLayout_noSearchView);

        productList = new ArrayList<>();
        productCategoryList = new ArrayList<>();
        filteredProductList = new ArrayList<>();
        popularProductList = new ArrayList<>();
        categorizedProductList = new ArrayList<>();
        groceryList = new ArrayList<>();
        storeLayoutList = new ArrayList<>();
        recommendedProductList = new ArrayList<>();
        graphEdges = new ArrayList<>();
        graphNodes = new ArrayList<>();

        popularProductsAdapter = new PopularProductsAdapter(this, popularProductList, this);
        categoryAdapter = new CategoryAdapter(this, productCategoryList, this);
        filteredProductsAdapter = new FilteredProductsAdapter(this, filteredProductList, this);
        categorizedProductsAdapter = new CategorizedProductsAdapter(this, categorizedProductList, this);
        groceryListAdapter = new GroceryListAdapter(this, groceryList, this);
        storeLayoutAdapter = new StoreLayoutAdapter(this, storeLayoutList);
        recommendedProductAdapter = new RecommendedProductAdapter(this, recommendedProductList, this);

        // Populate List Data
        readStoreLayoutFile();
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
        searchViewSearchProducts.setQueryHint(searchHints[(int)(Math.random() * 27 + 1)]);
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
                    FilteredProductsFragment filteredProductsFragment = new FilteredProductsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout_withSearchView, filteredProductsFragment)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }

                return true;
            }
        });

        // Implement Home Button Listeners
        imageButtonHome = findViewById(R.id.image_button_logo);
        imageButtonHome.setOnClickListener(v -> {
            if(currentlyAtCart == 1 || currentlyAtCart == 3){
                currentlyAtCart = 0;

                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                        .replace(R.id.frameLayout_noSearchView, new Fragment())
                        .commit();

                HomeFragment homeFragment12 = new HomeFragment();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                        .replace(R.id.frameLayout_withSearchView, homeFragment12)
                        .commit();
            }
            else if(currentlyAtCart == 2){
                currentlyAtCart = 0;

                HomeFragment homeFragment12 = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                        .replace(R.id.frameLayout_withSearchView, homeFragment12)
                        .commit();
                closeKeyboard();
                layout.setVisibility(View.GONE);
                searchViewSearchProducts.setQuery("", false);
                searchViewSearchProducts.clearFocus();
            }
        });

        // Implement Cart Button Listeners
        imageButtonCart = findViewById(R.id.image_button_cart);
        imageButtonCart.setOnClickListener(v -> {
            if(currentlyAtCart != 1) {
                currentlyAtCart = 1;

                imageButtonBack.setVisibility(View.INVISIBLE);

                GroceryListFragment groceryListFragment = new GroceryListFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_top)
                        .replace(R.id.frameLayout_noSearchView, groceryListFragment)
                        .commit();

                Handler handler = new Handler();
                handler.postDelayed(() -> fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout_withSearchView, new Fragment())
                        .commit(), 500);

                closeKeyboard();
                layout.setVisibility(View.VISIBLE);
            }
        });

        // Implement Back Button Listeners
        imageButtonBack = findViewById(R.id.imageButton_back);
        imageButtonBack.setOnClickListener(v -> {
            if(currentlyAtCart == 3){
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                        .replace(R.id.frameLayout_noSearchView, new Fragment())
                        .commit();

                HomeFragment homeFragment1 = new HomeFragment();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_top, R.anim.enter_from_top, R.anim.exit_to_bottom)
                        .replace(R.id.frameLayout_withSearchView, homeFragment1)
                        .commit();
            }
            else{
                fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                closeKeyboard();
            }
        });

        imageButtonFilter = findViewById(R.id.imageButton_filter);
        imageButtonFilter.setOnClickListener(v -> {
            if(filterOn){
                filterOn = false;
                imageButtonFilter.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_filter_alt_off_24, null));
                imageButtonFilter.setColorFilter(getResources().getColor(R.color.light_gray));

                setUpProductList();
                setUpPopularProductList();
            }
            else{
                if(budget != 0){
                    filterOn = true;
                    imageButtonFilter.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_filter_alt_24, null));
                    imageButtonFilter.setColorFilter(getResources().getColor(R.color.orange));

                    setUpBudgetedList();
                    setUpBudgetedPopularProductList();
                }
                else{
                    Snackbar.make(findViewById(R.id.coordinator_layout_main), "Please set a budget first.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        // Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Set a budget for your list");

        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        FrameLayout frame = new FrameLayout(this);
        FrameLayout.LayoutParams parameters = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parameters.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        parameters.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        input.setLayoutParams(parameters);

        frame.addView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            if(input.getText().toString().equals("") || input.getText().toString().equals(" ")){
                budget = 0;
            }
            else{
                budget = Double.parseDouble(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.setView(frame);

        Handler handler = new Handler();
        handler.postDelayed(builder::show, 1500);
    }

    // Function to Retrieve All Products from Database
    public void setUpProductList(){
        productList.clear();

        CollectionReference productCollection = db.collection("BranchName_Products");
        productCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProductInformationClass info = d.toObject(ProductInformationClass.class);
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
    public void setUpPopularProductList(){
        popularProductList.clear();

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
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    public void setUpBudgetedList(){
        productList.clear();

        CollectionReference productCollection = db.collection("BranchName_Products");
        productCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProductInformationClass info = d.toObject(ProductInformationClass.class);
                            if(Double.parseDouble(info.getProductPrice()) <= (budget - totalPrice)){
                                productList.add(info);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    public void setUpBudgetedPopularProductList(){
        popularProductList.clear();

        CollectionReference productCollection = db.collection("BranchName_Products");
        productCollection.whereEqualTo("productInStock", 1).whereEqualTo("productPopular", 1).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProductInformationClass info = d.toObject(ProductInformationClass.class);
                            if(Double.parseDouble(info.getProductPrice()) <= (budget - totalPrice)){
                                popularProductList.add(info);
                            }
                        }
                        popularProductsAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error in retrieving data.", Toast.LENGTH_SHORT).show());
    }

    // Function to Store Layout from Text File to ArrayList
    public void readStoreLayoutFile(){
        AssetManager assetManager = getAssets();

        try{
            String str;
            InputStream inputStream = assetManager.open("layout.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while((str = bufferedReader.readLine()) != null){
                stringBuilder.append(str).append(" ");
            }

            mapLayout = stringBuilder.toString().split(" ");
        }
        catch (Exception e){
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

        for(int i = 0; i < numberOfRows; i++){
            for(int j = 0; j < numberOfColumns; j++){
                storeLayoutList.add(new StoreLayoutClass(i, j, Integer.parseInt(mapLayout[(i * numberOfColumns) + j])));
            }
        }

        storeLayoutAdapter.notifyDataSetChanged();
    }

    // Function to Store All Category Information in an ArrayList
    public void setUpProductCategoryList(){
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
            productCategoryList.add(new CategoryClass(productCategoryNames[i], productCategoryImages[i]));
        }
        categoryAdapter.notifyDataSetChanged();
    }

    // Function to Close Device Keyboard
    public void closeKeyboard()
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

        productCategory = productCategoryList.get(position).getCategoryName();
        categoryNumber = position;

        imageButtonBack.setVisibility(View.VISIBLE);
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
    public void onPopularProductClick(int position) {
        currentlyAtCart = 2;

        productName = popularProductList.get(position).getProductName();
        productQuantity = 1;

        imageButtonBack.setVisibility(View.VISIBLE);
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCategorizedProductClick(int position) {
        currentlyAtCart = 2;

        productName = categorizedProductList.get(position).getProductName();
        productQuantity = 1;

        imageButtonBack.setVisibility(View.VISIBLE);
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
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
        imageButtonBack.setVisibility(View.VISIBLE);
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onQuantityAdd(int position) {
        groceryList.get(position).productQuantity++;
        totalPrice += Double.parseDouble(groceryList.get(position).productPrice);

        GroceryListFragment groceryListFragment = new GroceryListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout_noSearchView, groceryListFragment)
                .commit();
        closeKeyboard();
        layout.setVisibility(View.VISIBLE);

        if(filterOn){
            setUpBudgetedList();
            setUpBudgetedPopularProductList();
        }
    }

    @Override
    public void onQuantitySubtract(int position) {
        if(groceryList.get(position).productQuantity > 0){
            totalPrice -= Double.parseDouble(groceryList.get(position).productPrice);
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

        if(filterOn){
            setUpBudgetedList();
            setUpBudgetedPopularProductList();
        }
    }

    @Override
    public void onRecommendedProductsClick(int position) {
        currentlyAtCart = 2;

        productName = recommendedProductList.get(position).getProductName();
        productQuantity = 1;

        closeKeyboard();
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.frameLayout_withSearchView, productDetailsFragment)
                .commit();
    }
}