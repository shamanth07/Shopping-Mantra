package com.example.shoppingmantra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.shoppingmantra.Adapter.PopularAdapter;
import com.example.shoppingmantra.R;
import com.example.shoppingmantra.databinding.ActivityMainBinding;
import com.example.shoppingmantra.domain.PopularDomain;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private boolean isGuest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isGuest = getIntent().getBooleanExtra("isGuest", false);

        statusBarColor();
        initRecyclerView();
        bottomNavigation();
    }

    private void bottomNavigation() {
        if (isGuest) {
            binding.cartBtn.setEnabled(false);
            binding.cartBtn.setAlpha(0.5f);
            binding.cartBtn.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "Please log in to access the cart.", Toast.LENGTH_SHORT).show();
            });
        } else {
            binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        }
    }

    private void statusBarColor() {
        Window window = MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purple_Dark));
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("T-shirt black", "item_1", 15, 4, 500, "A stylish black T-shirt."));
        items.add(new PopularDomain("Smart Watch", "item_2", 10, 4.5, 450, "A sleek and modern smart watch."));
        items.add(new PopularDomain("Phone", "item_3", 3, 4.9, 800, "A high-performance smartphone."));

        binding.PopularView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.PopularView.setAdapter(new PopularAdapter(items));
    }
}
