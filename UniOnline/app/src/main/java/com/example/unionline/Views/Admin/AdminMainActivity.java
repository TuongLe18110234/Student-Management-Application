package com.example.unionline.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.unionline.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMainActivity extends AppCompatActivity {

   private BottomNavigationView bottomNav;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        // Set event listener for bottomNav
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mData = FirebaseDatabase.getInstance().getReference();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = (@NonNull MenuItem item) -> {
        Fragment selectedFragment = null;

        // Set selected fragment by corresponding fragment
        switch (item.getItemId()) {
            case R.id.nav_admin_home:
                selectedFragment = new AdminAccountFragment();
                break;
            case R.id.nav_admin_notification:
                selectedFragment = new AdminNotificationFragment();
                break;
            case R.id.nav_admin_account:
                selectedFragment = new AdminPersonalInfoFragment();
                break;
        }
        // Replace fragment container with selected fragment

        return true;
    };
}