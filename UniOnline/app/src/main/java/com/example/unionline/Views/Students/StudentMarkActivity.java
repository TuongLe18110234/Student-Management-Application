package com.example.unionline.Views.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.EnrollmentAdapter;
import com.example.unionline.Adapters.Students.MarkAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentMarkActivity extends AppCompatActivity  {

    RecyclerView recyclerView;
    ArrayList<Enrollment> enrollments;
    MarkAdapter adapter;
    DatabaseReference mDatabase;

    ImageView backIcon;
    TextView tvActivityName, tvAvgMark, tvRate;

    private MarkAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mark);

        // Set activity name on toolbar
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText("Điểm số");

        tvAvgMark = findViewById(R.id.tvAvgMark);
        tvRate = findViewById(R.id.tvRate);

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set event click for item in list enrollments
        setOnClickListener();
        setRecyclerView();
    }

    private void setOnClickListener() {
        listener = new MarkAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View v, int position) {
            }
        };
    }

    // Set data for recyc\erView
    private void setRecyclerView() {
        recyclerView = findViewById(R.id.rvListMark);

        enrollments  = new ArrayList<>();

        adapter = new MarkAdapter(this, enrollments, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set adapter for recycler view
        recyclerView.setAdapter(adapter);

        /**
         *
         *Fill data from Firebase
         * With student id
        */
        mDatabase = FirebaseDatabase.getInstance().getReference("Enrollments").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("studentId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrollments.clear();
                // avgMark to calculate mark form enrollment
                double avgMark = 0;
                int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    enrollments.add(enrollment);

                    // If mask is entered, add to avgMark
                    if(enrollment.getStateMark() == 1){
                        avgMark += (enrollment.getMidScore() + enrollment.getFinalScore())/2;
                        count++;
                    }
                }
                adapter.notifyDataSetChanged();

                avgMark = avgMark/count;
                tvAvgMark.setText(String.valueOf((Math.round(avgMark * 100.0) / 100.0)));

                // Set rate by avrMark
                String rate;
                if(avgMark >= 9.0){
                    rate = "Xuất sắc";
                } else if(avgMark >= 8.0){
                    rate = "Giỏi";
                } else if(avgMark >= 6.5){
                    rate = "Khá";
                } else if(avgMark >= 5.0){
                    rate = "Trung bình";
                } else if(avgMark >= 0){
                    rate = "Yếu";
                } else {
                    rate = "Không xếp loại";
                }
                tvRate.setText(rate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}