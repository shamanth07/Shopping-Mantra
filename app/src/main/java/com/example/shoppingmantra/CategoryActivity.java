//package com.example.ecommerceapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
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
//public class CategoryActivity extends AppCompatActivity {
//
//    private TextView categoryTitle;
//    private RecyclerView productsList;
//    private DatabaseReference ProductsRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_category);
//
//        categoryTitle = findViewById(R.id.category_title);
//        productsList = findViewById(R.id.products_list);
//        productsList.setLayoutManager(new LinearLayoutManager(this));
//
//        // Get the selected category from intent
//        String category = getIntent().getStringExtra("category");
//        categoryTitle.setText(category);
//
//        // Reference to Products in Firebase
//        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
//
//        // Query products based on category
//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef.orderByChild("category").equalTo(category), Products.class)
//                        .build();
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductPrice.setText("Price: $" + model.getPrice());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//                        holder.itemView.setOnClickListener(view -> {
//                            Intent intent = new Intent(CategoryActivity.this, ProductDetailsActivity.class);
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
//        productsList.setAdapter(adapter);
//        adapter.startListening();
//    }
//}
package com.example.shoppingmantra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Model.Products;
import com.example.ecommerceapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryActivity extends AppCompatActivity {

    private TextView categoryTitle;
    private RecyclerView productsList;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryTitle = findViewById(R.id.category_title);
        productsList = findViewById(R.id.products_list);
        productsList.setLayoutManager(new LinearLayoutManager(this));

        // Get the selected category from the intent
        String category = getIntent().getStringExtra("category");
        Log.d("CategoryActivity", "Received category: " + category);

        // Normalize category to ensure case and space consistency
        String normalizedCategory = category.trim();

        // Display the category name
        categoryTitle.setText(category);

        // Reference to Products in Firebase
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        // Query products based on the normalized category
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef.orderByChild("category").equalTo(normalizedCategory), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        Log.d("CategoryActivity", "Loaded product: " + model.getPname());
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText("Price: $" + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(view -> {
                            Intent intent = new Intent(CategoryActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        return new ProductViewHolder(view);
                    }
                };

        productsList.setAdapter(adapter);
        adapter.startListening();

        // Debugging query result
        ProductsRef.orderByChild("category").equalTo(normalizedCategory)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("CategoryActivity", "Number of products: " + snapshot.getChildrenCount());
                        if (snapshot.getChildrenCount() == 0) {
                            Toast.makeText(CategoryActivity.this, "No products found in this category.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("CategoryActivity", "Error: " + error.getMessage());
                    }
                });
    }
}
