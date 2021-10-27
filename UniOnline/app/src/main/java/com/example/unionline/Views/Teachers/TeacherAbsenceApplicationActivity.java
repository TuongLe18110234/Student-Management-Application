package com.example.unionline.Views.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Teachers.ApplicationPagerAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Class;
import com.example.unionline.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherAbsenceApplicationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabPending, tabDone;
    private ApplicationPagerAdapter pagerAdapter;

    private ArrayList<String> classIds;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_absence_application);

        // Tool bar on the top of screen
        setToolbar();
        // Get list classId that teacher is teaching
        getClassIdsFromFirebase();

        // Mapping Tab layout, tab items and view pager
        tabLayout   = (TabLayout) findViewById(R.id.teacher_absenceApplication_tabLayout);
        tabPending  = (TabItem) findViewById(R.id.tab_pending);
        tabDone     = (TabItem) findViewById(R.id.tab_done);
        viewPager   = (ViewPager) findViewById(R.id.teacher_absenceApplication_viewPager);

        pagerAdapter = new ApplicationPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), classIds);
        viewPager.setAdapter(pagerAdapter);

        // Change page when selecting tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Get list classId of classes that teacher is teaching
    private void getClassIdsFromFirebase() {
        classIds = new ArrayList<>();
        // Fill data from Firebase
        // Only get class that have teacherId == id of current user
        mDataBase = FirebaseDatabase.getInstance().getReference("Classes").child(Common.semester.getSemesterId());
        Query query = mDataBase.orderByChild("teacherId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classIds.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Class aClass = dataSnapshot.getValue(Class.class);
                    classIds.add(aClass.getClassId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setToolbar() {
        // Finish activity when clicking on back icon
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set name for activity
        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText("Duyệt đơn xin nghỉ");
    }
}