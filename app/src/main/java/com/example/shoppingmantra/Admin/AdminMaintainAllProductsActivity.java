package com.example.shoppingmantra.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Model.Products;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminMaintainAllProductsActivity extends AppCompatActivity {

    private EditText searchInput;
    private ImageButton searchButton;
    private RecyclerView productList;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_all_products);

        searchInput = findViewById(R.id.search_product_name);
        searchButton = findViewById(R.id.search_btn);
        productList = findViewById(R.id.product_list);

        productList.setLayoutManager(new LinearLayoutManager(this));
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        fetchProducts("");

        searchButton.setOnClickListener(view -> {
            String query = searchInput.getText().toString().trim();
            fetchProducts(query);
        });
    }

    private void fetchProducts(String query) {
        FirebaseRecyclerOptions<Products> options;
        if (TextUtils.isEmpty(query)) {
            options = new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(productsRef, Products.class)
                    .build();
        } else {
            options = new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(productsRef.orderByChild("pname").startAt(query).endAt(query + "\uf8ff"), Products.class)
                    .build();
        }

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductPrice.setText("Price: $" + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(AdminMaintainAllProductsActivity.this, AdminMaintainProductsActivity.class);
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

        productList.setAdapter(adapter);
        adapter.startListening();
    }
}
