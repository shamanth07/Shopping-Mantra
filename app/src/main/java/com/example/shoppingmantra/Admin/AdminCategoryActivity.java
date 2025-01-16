package com.example.shoppingmantra.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.MainActivity;
import com.example.ecommerceapp.R;

public class AdminCategoryActivity extends AppCompatActivity {

    // Declare all category image views
    private ImageView tShirts, sportsTShirts, femaleDresses, sweaters;
    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headphonesHandFree, laptops, watches, mobilePhones;

    // Declare buttons
    private Button logoutBtn, maintainProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        // Initialize buttons
        logoutBtn = findViewById(R.id.admin_logout_btn);
        maintainProductsBtn = findViewById(R.id.maintain_btn);

        // Initialize category image views
        tShirts = findViewById(R.id.t_shirts);
        sportsTShirts = findViewById(R.id.sports_t_shirts);
        femaleDresses = findViewById(R.id.female_dresses);
        sweaters = findViewById(R.id.sweathers);
        glasses = findViewById(R.id.glasses);
        hatsCaps = findViewById(R.id.hats_caps);
        walletsBagsPurses = findViewById(R.id.purses_bags_wallets);
        shoes = findViewById(R.id.shoes);
        headphonesHandFree = findViewById(R.id.headphones_handfree);
        laptops = findViewById(R.id.laptop_pc);
        watches = findViewById(R.id.watches);
        mobilePhones = findViewById(R.id.mobilephones);

        // Set click listeners for each category
        setCategoryClickListener(tShirts, "T-Shirts");
        setCategoryClickListener(sportsTShirts, "Sports T-Shirts");
        setCategoryClickListener(femaleDresses, "Female Dresses");
        setCategoryClickListener(sweaters, "Sweaters");
        setCategoryClickListener(glasses, "Glasses");
        setCategoryClickListener(hatsCaps, "Hats");
        setCategoryClickListener(walletsBagsPurses, "Purses and Bags");
        setCategoryClickListener(shoes, "Shoes");
        setCategoryClickListener(headphonesHandFree, "Headphones");
        setCategoryClickListener(laptops, "Laptops");
        setCategoryClickListener(watches, "Watches");
        setCategoryClickListener(mobilePhones, "Mobile Phones");

        // Logout button functionality
        logoutBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Maintain Products button functionality
        maintainProductsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminUserProductsActivity.class);
            intent.putExtra("Admin", "Admin");
            startActivity(intent);
        });
    }

    private void setCategoryClickListener(ImageView categoryView, String categoryName) {
        categoryView.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", categoryName);
            startActivity(intent);
        });
    }
}
