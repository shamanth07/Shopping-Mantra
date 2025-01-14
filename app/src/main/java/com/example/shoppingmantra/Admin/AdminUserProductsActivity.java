package com.example.shoppingmantra.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingmantra.Model.Products;
import com.example.shoppingmantra.R;
import com.example.shoppingmantra.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView productsList;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        productsList.setLayoutManager(new LinearLayoutManager(this));

        // Reference to the Products node in Firebase
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        holder.txtProductName.setText("Name: " + model.getPname());
                        holder.txtProductPrice.setText("Price: $" + model.getPrice());
                        holder.txtProductDescription.setText("Description: " + model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        // Set onClickListener to navigate to AdminMaintainProductsActivity
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(AdminUserProductsActivity.this, AdminMaintainProductsActivity.class);
                                intent.putExtra("pid", model.getPid()); // Pass product ID
                                startActivity(intent);
                            }
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
    }
}
