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

import com.example.unionline.Adapters.Teachers.ClassAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Class;
import com.example.unionline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherAttendantActivity extends AppCompatActivity {

    DatabaseReference mData;
    List<Class> classes;
    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    ClassAdapter adapter;
    ClassAdapter.RecyclerViewClickListener listener;
    ImageView backIcon;
    TextView txtActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendant);

        // Set activity name on toolbar
        txtActivityName = (TextView) findViewById(R.id.activity_name);
        txtActivityName.setText("Danh sách lớp");

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set event click for item in list classes
        setOnClickListener();

        setRecyclerView();
    }

    // When teacher click on a class in list classes --> Create a activity to show list lessons
    private void setOnClickListener() {
        listener = new ClassAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View v, int position) {
                Intent intent = new Intent(TeacherAttendantActivity.this, TeacherListLessonsActivity.class);
                // Pun classId and className of selected class to bundle
                Bundle bundle = new Bundle();
                bundle.putString("classId", classes.get(position).getClassId());
                bundle.putString("className", classes.get(position).getClassName());
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onTouch(View v, int position) {
            }
        };
    }

    private void setRecyclerView() {
        // Initialize
        classes  = new ArrayList<>();
        adapter = new ClassAdapter(this, (ArrayList<Class>) classes, listener);
        gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        // Set adapter for recycler view
        recyclerView = findViewById(R.id.rvListClasses);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        // Fill data from Firebase
        // Data is list classes that teacher is teaching
        mData = FirebaseDatabase.getInstance().getReference("Classes").child(Common.semester.getSemesterId());
        Query query = mData.orderByChild("teacherId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Class aClass = dataSnapshot.getValue(Class.class);
                    classes.add(aClass);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}