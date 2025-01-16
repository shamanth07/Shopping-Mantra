//package com.example.ecommerceapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.ecommerceapp.Model.Products;
//import com.example.ecommerceapp.ViewHolder.ProductViewHolder;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.squareup.picasso.Picasso;
//
//public class SearchProductsActivity extends AppCompatActivity {
//
//    private EditText searchInput;
//    private Button searchButton;
//    private RecyclerView searchList;
//    private DatabaseReference productsRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_products);
//
//        // Initialize Views
//        searchInput = findViewById(R.id.search_product_name);
//        searchButton = findViewById(R.id.search_btn);
//        searchList = findViewById(R.id.search_list);
//
//        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
//
//        searchList.setLayoutManager(new LinearLayoutManager(this));
//
//        // Search Button Click Listener
//        searchButton.setOnClickListener(v -> {
//            String query = searchInput.getText().toString().trim();
//
//            if (TextUtils.isEmpty(query)) {
//                Toast.makeText(this, "Please enter a product name.", Toast.LENGTH_SHORT).show();
//            } else {
//                searchProducts(query);
//            }
//        });
//    }
//
//    private void searchProducts(String query) {
//        query = query.trim(); // Remove any leading/trailing spaces
//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(productsRef.orderByChild("pname").startAt(query).endAt(query + "\uf8ff"), Products.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductPrice.setText("Price: $" + model.getPrice());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//
//                        holder.itemView.setOnClickListener(v -> {
//                            Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
//                            intent.putExtra("pid", model.getPid());
//                            startActivity(intent);
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                        return new ProductViewHolder(view);
//                    }
//                };
//
//        searchList.setAdapter(adapter);
//        adapter.startListening();
//    }
//}

package com.example.shoppingmantra;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Model.Products;
import com.example.ecommerceapp.ViewHolder.ProductViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchProductsActivity extends AppCompatActivity {

    private EditText searchInput;
    private Button searchButton;
    private RecyclerView searchList;
    private DatabaseReference productsRef;

    private List<Products> productList; // To hold the fetched products
    private List<Products> filteredList; // To hold the search results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        // Initialize Views
        searchInput = findViewById(R.id.search_product_name);
        searchButton = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        searchList.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Load all products into memory
        loadAllProducts();

        // Search Button Click Listener
        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();

            if (TextUtils.isEmpty(query)) {
                Toast.makeText(this, "Please enter a product name.", Toast.LENGTH_SHORT).show();
            } else {
                searchProducts(query);
            }
        });
    }

    private void loadAllProducts() {
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Products product = productSnapshot.getValue(Products.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchProductsActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducts(String query) {
        query = query.toLowerCase(); // Convert query to lowercase for case-insensitive search

        filteredList.clear();

        // Filter the products for case-insensitive match
        for (Products product : productList) {
            if (product.getPname().toLowerCase().contains(query)) {
                filteredList.add(product);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No products found.", Toast.LENGTH_SHORT).show();
        }

        // Display filtered results
        displaySearchResults();
    }

    private void displaySearchResults() {
        RecyclerView.Adapter<ProductViewHolder> adapter = new RecyclerView.Adapter<ProductViewHolder>() {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                return new ProductViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
                Products model = filteredList.get(position);

                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price: $" + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("pid", model.getPid());
                    startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return filteredList.size();
            }
        };

        searchList.setAdapter(adapter);
    }
}
