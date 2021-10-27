package com.example.unionline.Views.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.unionline.DTO.Class;
import com.example.unionline.R;
import com.example.unionline.Views.Common.CommonAccountFragment;
import com.example.unionline.Views.Teachers.Fragments.TeacherHomeFragment;
import com.example.unionline.Views.Teachers.Fragments.TeacherInteractionFragment;
import com.example.unionline.Views.Teachers.Fragments.TeacherNotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.teacher_fragment_container, new TeacherHomeFragment()).commit();

        // Mapping bottomNav with view
        bottomNav = findViewById(R.id.teacher_bottom_navigation);

        // Set event listener for bottomNav
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mData = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Change Fragment when selecting item on Bottom Navigation Bar
     * When user selects an item on bottom navigation bar, fragment container will be replaced with
     * corresponding fragment
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = (@NonNull MenuItem item) -> {
                Fragment selectedFragment = null;

                // Set selected fragment by corresponding fragment
                switch (item.getItemId()) {
                    case R.id.nav_teacher_home:
                        selectedFragment = new TeacherHomeFragment();
                        break;
                    case R.id.nav_teacher_interaction:
                        selectedFragment = new TeacherInteractionFragment();
                        break;
                    case R.id.nav_teacher_notification:
                        selectedFragment = new TeacherNotificationFragment();
                        break;
                    case R.id.nav_teacher_account:
                        selectedFragment = new CommonAccountFragment();
                        break;
                }
                // Replace fragment container with selected fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.teacher_fragment_container, selectedFragment).commit();
                return true;
            };
}