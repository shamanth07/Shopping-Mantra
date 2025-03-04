package com.example.shoppingmantra;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference UsersRef, ProductsRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String type = "";
    private GridLayout categoryGrid;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve intent extras to check if it's an Admin login
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = bundle.getString("Admin", "");
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!type.equals("Admin")) {
                Intent intent1 = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent1);
            } else {
                Toast.makeText(HomeActivity.this, "Admin cannot access the cart.", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        // Fetch user details from Firebase
        if (currentUser != null && !type.equals("Admin")) {
            String userId = currentUser.getUid();
            UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

            UsersRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("fullName").getValue(String.class);
                        String profileImage = snapshot.child("profileImage").getValue(String.class);

                        userNameTextView.setText(fullName != null ? fullName : "User Name");
                        if (profileImage != null) {
                            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Handle Category Grid Clicks
        categoryGrid = findViewById(R.id.category_grid);
        for (int i = 0; i < categoryGrid.getChildCount(); i++) {
            View categoryView = categoryGrid.getChildAt(i);
            categoryView.setOnClickListener(this::openCategory);
        }
    }

    // Method to open category-specific product list
    public void openCategory(View view) {
        String category = (String) view.getTag();
        Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            if (!type.equals("Admin")) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            } else {
                Toast.makeText(this, "Admin cannot access the Cart.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_search) {
            if (!type.equals("Admin")) {
                startActivity(new Intent(HomeActivity.this, SearchProductsActivity.class));
            }
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(HomeActivity.this, AccountActivity.class));
        } else if (id == R.id.nav_wishlist) {
            startActivity(new Intent(HomeActivity.this, WishlistActivity.class));
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
