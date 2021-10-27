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
import android.widget.TextView;

import com.example.unionline.Adapters.Students.AttendanceAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Semester;
import com.example.unionline.R;
import com.example.unionline.Views.Students.StudentAttendanceActivity;
import com.example.unionline.Views.Students.StudentScheduleActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

public class StudentHomeFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Attendance> listAttendance;
    AttendanceAdapter attendanceAdapter;
    DatabaseReference mDatabase;

    String currentDate;
    TextView tvSchedule;

    private AttendanceAdapter.RecyclerViewClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_student_home, container, false);

        // Set for activity
        setOnClickListener();
        setRecyclerView(root);

        // Get current date to set for text view
        currentDate = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString();

        // Set current date for text view
        TextView tvCurrentDate = root.findViewById(R.id.tvCurrentDate);
        tvCurrentDate.setText(currentDate);

        // Set schedule
        tvSchedule = root.findViewById(R.id.tvSchedual);
        tvSchedule.setOnClickListener((View v) -> {
            Intent intent = new Intent(this.getActivity(), StudentScheduleActivity.class);
            startActivity(intent);
        });

        return root;
    }

    /**
     *
     * Set on click for item RecyclerView
     */
    private void setOnClickListener() {
        listener = new AttendanceAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                // Start attendance
                Intent intent = new Intent(getActivity(), StudentAttendanceActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("attendance", (Serializable) listAttendance.get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };
    }

    /**
     * Initial data for recyclerview and set data
     * @param root
     */
    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvAttendance);

        listAttendance = new ArrayList<>();

        attendanceAdapter = new AttendanceAdapter(getContext(), listAttendance, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(attendanceAdapter);

        // Get data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Attendances").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("studentId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAttendance.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);

                    // Compare date attendance with current date to add to list attendance
                    if(attendance.getFullDate()!=null){
                        if(attendance.getFullDate().equals(currentDate)){
                            listAttendance.add(attendance);
                        }
                    }
                }
                attendanceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}