package com.example.unionline.Views.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Teachers.ClassProcessAdapter;
import com.example.unionline.Common;
import com.example.unionline.DAO.LessonDAO;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class TeacherListLessonsActivity extends AppCompatActivity {

    private static final String ARG_CLASS_ID = "classId";
    private static final String ARG_CLASS_NAME = "className";

    private String classId, className;
    private TextView tvActivityName;
    private ImageView backIcon;

    RecyclerView recyclerView;
    ArrayList<Lesson> lessons;
    ClassProcessAdapter classProcessAdapter;
    GridLayoutManager gridLayoutManager;
    DatabaseReference mData;

    private ClassProcessAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list_lessons);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            classId = bundle.getString(ARG_CLASS_ID);
            className = bundle.getString(ARG_CLASS_NAME);
        }

        setToolbar();
        setOnClickListener();
        setRecyclerView();
    }

    // Open activity attendant when clicking on lesson
    private void setOnClickListener() {
        listener = new ClassProcessAdapter.RecyclerViewClickListener() {

            @Override
            public void onTouch(View v, int adapterPosition) {
                // do nothing
            }

            @Override
            public void onCLick(View itemView, int adapterPosition) {
                Lesson lesson = lessons.get(adapterPosition);

                Intent intent = new Intent(TeacherListLessonsActivity.this, TeacherAttendantUpdateActivity.class);
                // Put lessonId and week to bundle
                Bundle bundle = new Bundle();
                bundle.putString("lessonId", lesson.getLessonId());
                bundle.putInt("week", lesson.getWeek());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };
    }

    private void setRecyclerView() {

        // Initialize
        lessons = new ArrayList<>();
        classProcessAdapter = new ClassProcessAdapter(this, (ArrayList<Lesson>) lessons, listener);
        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        // Set adapter for recycler view
        recyclerView = findViewById(R.id.rvListLessonProcess);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(classProcessAdapter);

        // Fill data from Firebase
        mData = FirebaseDatabase.getInstance().getReference("Lessons").child(Common.semester.getSemesterId());
        Query query = mData.orderByChild("classId").equalTo(classId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                lessons.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    lessons.add(lesson);
                }
                classProcessAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void setToolbar() {
        // Set activity name on toolbar
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText(className);

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });
    }
}