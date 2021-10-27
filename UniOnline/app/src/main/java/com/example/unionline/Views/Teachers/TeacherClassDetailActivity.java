package com.example.unionline.Views.Teachers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Adapters.Teachers.ClassPagerAdapter;
import com.example.unionline.DTO.Class;
import com.example.unionline.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TeacherClassDetailActivity extends AppCompatActivity {

    private TextView txtActivityName;
    private ImageView backIcon;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabClassInfo, tabUpdateProcess, tabMark;
    public ClassPagerAdapter pageAdapter;
    private Class currentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_detail);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentClass = (Class) bundle.getSerializable("class");
        }

        // Set activity name on toolbar
        txtActivityName = (TextView) findViewById(R.id.activity_name);
        txtActivityName.setText(currentClass.getClassName());

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Mapping TabLayout, TabItem and ViewPager
        tabLayout           = (TabLayout) findViewById(R.id.teacher_class_tabLayout);
        tabClassInfo        = (TabItem) findViewById(R.id.tab_classInfo);
        tabUpdateProcess    = (TabItem) findViewById(R.id.tab_updateProcess);
        tabMark             = (TabItem) findViewById(R.id.tab_mark);
        viewPager           = (ViewPager) findViewById(R.id.teacher_class_viewPager);

        // Set Adapter for ViewPager
        pageAdapter = new ClassPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), currentClass);
        viewPager.setAdapter(pageAdapter);

        // Set event onTabSelected
        // Get position of tab when selected --> update view
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2) {
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set event onPageChange
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}