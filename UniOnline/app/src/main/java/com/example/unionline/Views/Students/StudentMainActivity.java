package com.example.unionline.Views.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.unionline.R;
import com.example.unionline.Views.Common.CommonAccountFragment;
import com.example.unionline.Views.Students.Fragments.StudentHomeFragment;
import com.example.unionline.Views.Students.Fragments.StudentInteractionFragment;
import com.example.unionline.Views.Students.Fragments.StudentNotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.student_fragment_container, new StudentHomeFragment()).commit();

        bottomNav = findViewById(R.id.student_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    /**
     * Set navigation click
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_student_home:
                            selectedFragment = new StudentHomeFragment();
                            break;
                        case R.id.nav_student_interaction:
                            selectedFragment = new StudentInteractionFragment();
                            break;
                        case R.id.nav_student_notification:
                            selectedFragment = new StudentNotificationFragment();
                            break;
                        case R.id.nav_student_account:
                            selectedFragment = new CommonAccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.student_fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}