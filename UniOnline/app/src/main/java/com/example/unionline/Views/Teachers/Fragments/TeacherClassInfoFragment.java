package com.example.unionline.Views.Teachers.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Common;
import com.example.unionline.DAO.ClassDAO;
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
import java.util.List;

public class TeacherClassInfoFragment extends Fragment {

    private static final String ARG_CLASS = "class";
    DatabaseReference mDataBase;

    private Class aClass;
    private TextView tvClassName, tvClassId, tvStartDate, tvEndDate, tvStatus, tvRoom, tvTime, tvProcess;

    List<Lesson> lessons = new ArrayList<>();

    public TeacherClassInfoFragment() {
        // Required empty public constructor
    }

    public static TeacherClassInfoFragment newInstance(Class aClass) {
        TeacherClassInfoFragment fragment = new TeacherClassInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS, (Serializable) aClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Get selected class object from Bundle
            aClass = (Class) getArguments().getSerializable(ARG_CLASS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_class_info, container, false);

        // Map variables with view
        mapView(root);
        // Set value
        setValueForView();

        return root;
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
        // Get values from selected class
        // Set values for view
        tvClassName.setText(aClass.getClassName());
        tvClassId.setText(aClass.getClassId());
        tvStartDate.setText(aClass.getStartDate());
        tvEndDate.setText(aClass.getEndDate());
        tvRoom.setText(aClass.getRoom());
        tvStatus.setText(aClass.getState());
        tvTime.setText("Từ tiết " + aClass.getStartTime() + " đến tiết " + aClass.getEndTime());

        // Get list lessons in selected class
        mDataBase = FirebaseDatabase.getInstance().getReference("Lessons").child(aClass.getSemesterId());
        Query query = mDataBase.orderByChild("classId").equalTo(aClass.getClassId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                lessons.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    lessons.add(lesson);
                }

                // Set class process info
                // process = #learnedLesson/#totalLessons
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