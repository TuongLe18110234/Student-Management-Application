package com.example.unionline.Views.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.NotificationAdapter;
import com.example.unionline.Adapters.Teachers.UpdateAttendantAdapter;
import com.example.unionline.Common;
import com.example.unionline.DAO.AttendanceDAO;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Notification;
import com.example.unionline.R;
import com.example.unionline.Sorter.NotificationDateSorter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TeacherAttendantUpdateActivity extends AppCompatActivity {

    private String currentLessonId;
    private int week;

    RecyclerView recyclerView;
    ArrayList<Attendance> lisAttendances;
    UpdateAttendantAdapter adapter;
    DatabaseReference mDatabase;

    private TextView tvSum, tvLate, tvWithPermission, tvWithoutPermission, tvActivityName, tvWeek;
    private RadioButton rbOnTime, rbLate, rbWithPermission, rbWithoutPermission;
    private ImageView backIcon;
    private int sum, lateCount, withPermissionCount, withoutPermissionCount;
    private UpdateAttendantAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendant_update);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentLessonId = bundle.getString("lessonId");
            week = bundle.getInt("week");
        }

        mappingView();
        setOnClickListener();
        setRecyclerView();
    }

    // Mapping
    private void mappingView(){
        setToolbar();

        tvWeek = (TextView) findViewById(R.id.tvWeek);
        tvWeek.setText(String.valueOf(week));

        tvSum = (TextView) findViewById(R.id.txtSum);
        tvLate = (TextView) findViewById(R.id.txtLate);
        tvWithPermission = (TextView) findViewById(R.id.txtWithPermission);
        tvWithoutPermission = (TextView) findViewById(R.id.txtWithoutPermission);
    }

    private void setToolbar() {
        // Set activity name on toolbar
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText("Điểm danh");

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });
    }

    // Event when teacher click on one of radio buttons
    private void setOnClickListener() {
        listener = new UpdateAttendantAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View itemView, int adapterPosition) {
                // Get selected attendance
                Attendance attendance = lisAttendances.get(adapterPosition);

                // Mapping
                rbOnTime = (RadioButton) itemView.findViewById(R.id.rbOnTime);
                rbLate = (RadioButton) itemView.findViewById(R.id.rbLate);
                rbWithPermission = (RadioButton) itemView.findViewById(R.id.rbWithPermission);
                rbWithoutPermission = (RadioButton) itemView.findViewById(R.id.rbWithoutPermission);

                if (rbOnTime.isChecked()) {
                    // On time
                    attendance.setState(Common.ATTENDANCE_ON_TIME);
                } else if (rbLate.isChecked()) {
                    // Late
                    attendance.setState(Common.ATTENDANCE_LATE);
                } else if (rbWithPermission.isChecked()) {
                    // Absent with permission
                    attendance.setState(Common.ATTENDANCE_WITH_PERMISSION);
                } else if (rbWithoutPermission.isChecked()) {
                    // Absent without permission
                    attendance.setState(Common.ATTENDANCE_WITHOUT_PERMISSION);
                } else {
                    // Haven't checked yet
                    attendance.setState(Common.ATTENDANCE_NOT_YET);
                }

                AttendanceDAO.getInstance().changeAttendanceState(attendance);

            }
        };
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.rvAttendance);
        lisAttendances = new ArrayList<>();
        adapter = new UpdateAttendantAdapter(this, lisAttendances, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        // Get data from firebase
        // Data is list attendances that have lessonId = current lesson id
        mDatabase = FirebaseDatabase.getInstance().getReference("Attendances").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("lessonId").equalTo(currentLessonId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lisAttendances.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);
                    lisAttendances.add(attendance);
                }
                adapter.notifyDataSetChanged();
                updateStatistic();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateStatistic() {
        // #Late
        lateCount = lisAttendances
                .stream().filter(attendance -> attendance.getState() == Common.ATTENDANCE_LATE)
                .collect(Collectors.toList()).size();
        // #Absent with permission
        withPermissionCount = lisAttendances
                .stream().filter(attendance -> attendance.getState() == Common.ATTENDANCE_WITH_PERMISSION)
                .collect(Collectors.toList()).size();
        // #Absent without permission
        withoutPermissionCount = lisAttendances
                .stream().filter(attendance -> attendance.getState() == Common.ATTENDANCE_WITHOUT_PERMISSION)
                .collect(Collectors.toList()).size();
        // sum = #ontime + #late
        sum = lisAttendances
                .stream().filter(attendance -> attendance.getState() == Common.ATTENDANCE_ON_TIME)
                .collect(Collectors.toList()).size() + lateCount;

        // Set value to summary header
        tvSum.setText(String.valueOf(sum) + "/" + String.valueOf(lisAttendances.size()));
        tvLate.setText(String.valueOf(lateCount));
        tvWithPermission.setText(String.valueOf(withPermissionCount));
        tvWithoutPermission.setText(String.valueOf(withoutPermissionCount));
    }
}