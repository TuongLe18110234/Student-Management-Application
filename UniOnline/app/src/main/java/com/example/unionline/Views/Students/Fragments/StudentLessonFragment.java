package com.example.unionline.Views.Students.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unionline.Adapters.Students.LessonAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;
import com.example.unionline.Views.Students.StudentAttendanceActivity;
import com.example.unionline.Views.Students.StudentLessonActivity;
import com.example.unionline.Views.Students.StudentScheduleActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentLessonFragment extends Fragment {
    private static final String ARG_ENROLLMENT = "enrollment";
    
    RecyclerView recyclerView;
    ArrayList<Lesson> lessons;
    LessonAdapter adapter;
    DatabaseReference mDatabase;

    Enrollment enrollment;

    private LessonAdapter.RecyclerViewClickListener listener;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            enrollment = (Enrollment) getArguments().getSerializable(ARG_ENROLLMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_student_lesson, container, false);

        // Set event click for item in list lessons
        setOnClickListener();
        setRecyclerView(view);
        
        return view;
    }

    // Set onclick for item recycler view
    private void setOnClickListener() {
        listener = new LessonAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View v, int position) {
                // Click on RecyclerView item to see detail info
                Intent intent = new Intent(getActivity(), StudentLessonActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("lesson", (Serializable) lessons.get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };
    }

    private void setRecyclerView(View v) {
        // Initialize
        recyclerView = v.findViewById(R.id.rvListLesson);

        lessons  = new ArrayList<>();

        adapter = new LessonAdapter(getContext(), lessons, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set adapter for recycler view
        recyclerView.setAdapter(adapter);

        // Fill data from Firebase with list lesson of class
        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("classId").equalTo(enrollment.getClassId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessons.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    lessons.add(lesson);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}