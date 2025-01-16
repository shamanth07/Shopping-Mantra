package com.example.shoppingmantra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingmantra.Helper.ManagmentCart;
import com.example.shoppingmantra.R;
import com.example.shoppingmantra.databinding.ActivityDetailBinding;
import com.example.shoppingmantra.domain.PopularDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private PopularDomain object;
    private int numberOrder = 1;
    private ManagmentCart managmentCart;
    private DatabaseReference wishlistRef;
    private boolean isGuest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isGuest = getIntent().getBooleanExtra("isGuest", false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (!isGuest && user != null) {
            String userId = user.getUid();
            managmentCart = new ManagmentCart(this, userId);
            wishlistRef = FirebaseDatabase.getInstance().getReference("wishlists").child(userId);
        } else {
            isGuest = true;
            managmentCart = null;
            wishlistRef = null;
        }

        getBundles();
        statusBarColor();
    }

    private void statusBarColor() {
        Window window = DetailActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(DetailActivity.this, R.color.white));
    }

    private void getBundles() {
        object = (PopularDomain) getIntent().getSerializableExtra("object");

        int drawableResourceId = this.getResources().getIdentifier(object.getPicUrl(), "drawable", this.getPackageName());
        Glide.with(this).load(drawableResourceId).into(binding.itemPic);

        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.descriptionTxt.setText(object.getDescription());

        // Handle Add to Cart button
        binding.addToCardBtn.setOnClickListener(v -> {
            if (isGuest) {
                Toast.makeText(this, "Please create a profile to add anything to cart.", Toast.LENGTH_SHORT).show();
            } else {
                object.setNumberInCart(numberOrder);
                managmentCart.insertFood(object);
                Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Save to Wishlist button
        binding.imageView8.setOnClickListener(v -> {
            if (isGuest) {
                Toast.makeText(this, "Please log in to save items to your wishlist.", Toast.LENGTH_SHORT).show();
            } else {
                addToWishlist();
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void addToWishlist() {
        if (wishlistRef != null) {
            wishlistRef.child(object.getTitle().replace(" ", "_")).setValue(object)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Added to wishlist!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to add to wishlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
