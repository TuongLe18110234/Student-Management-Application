package com.example.unionline.Views.Students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.PageAdapter;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class StudentClassDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabClassInfo, tabLesson;
    public PageAdapter pageAdapter;
    private Enrollment currentEnrollment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail_class);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentEnrollment = (Enrollment) bundle.getSerializable("enrollment");
        }

        setToolBar(currentEnrollment.getClassName());

        // Mapping TabLayout, TabItem and ViewPager
        tabLayout = (TabLayout) findViewById(R.id.student_class_tabLayout);
        tabClassInfo = (TabItem) findViewById(R.id.tab_classInfo);
        tabLesson = (TabItem) findViewById(R.id.tab_lesson);
        viewPager = (ViewPager) findViewById(R.id.student_class_viewPager);

        // Set Adapter for ViewPager
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), currentEnrollment);
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

    private void setToolBar(String name){
        // Set activity name on toolbar
        TextView tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText(name);

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        ImageView backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });
    }
}