package com.example.unionline.Views.Students.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unionline.Common;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StudentClassInfoFragment extends Fragment {

    private static final String ARG_ENROLLMENT = "enrollment";

    private Enrollment enrollment;
    private TextView tvClassName, tvClassId, tvStartDate, tvEndDate, tvStatus, tvRoom, tvTime, tvProcess;
    DatabaseReference mDatabase;

    List<Lesson> lessons = new ArrayList<>();

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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student_class_info, container, false);

        mapView(view);
        if(enrollment != null) {
            setValueForView();
        }

        return view;
    }

    private void mapView(View root) {
        // Mapping variables with view
        tvClassName = (TextView) root.findViewById(R.id.txtClassName);
        tvClassId   = (TextView) root.findViewById(R.id.txtClassId);
        tvStartDate = (TextView) root.findViewById(R.id.txtStartDateValue);
        tvEndDate   = (TextView) root.findViewById(R.id.txtEndDateValue);
        tvStatus    = (TextView) root.findViewById(R.id.txtStatusValue);
        tvRoom      = (TextView) root.findViewById(R.id.txtRoomValue);
        tvTime      = (TextView) root.findViewById(R.id.txtTimeValue);
        tvProcess   = (TextView) root.findViewById(R.id.txtProcessValue);
    }

    private void setValueForView() {
        // Get class
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Classes").child(Common.semester.getSemesterId()).child(enrollment.getClassId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    Class aClass = task.getResult().getValue(Class.class);

                    if(aClass != null) {
                        tvClassName.setText(aClass.getClassName());
                        tvClassId.setText(aClass.getClassId());
                        tvStartDate.setText(aClass.getStartDate());
                        tvEndDate.setText(aClass.getEndDate());
                        tvRoom.setText(aClass.getRoom());
                        tvStatus.setText(aClass.getState());
                        tvTime.setText("Từ " + aClass.getStartTime() + " đến " + aClass.getEndTime());
                    }
                }
            }
        });

        // Get list lesson to count process
        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("classId").equalTo(enrollment.getClassId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                lessons.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    lessons.add(lesson);
                }

                int totalLesson = lessons.size();
                long learnedLesson = lessons.stream().filter(c -> c.isStatus() == true).count();
                tvProcess.setText(learnedLesson + "/" + totalLesson + " buổi");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}