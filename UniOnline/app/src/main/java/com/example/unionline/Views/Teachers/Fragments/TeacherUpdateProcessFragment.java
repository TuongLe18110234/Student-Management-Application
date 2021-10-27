package com.example.unionline.Views.Teachers.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Adapters.Teachers.ClassProcessAdapter;
import com.example.unionline.Common;
import com.example.unionline.DAO.EnrollmentDAO;
import com.example.unionline.DAO.LessonDAO;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Enrollment;
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

public class TeacherUpdateProcessFragment extends Fragment {

    private static final String ARG_CLASS = "class";
    private Class aClass;

    RecyclerView recyclerView;
    ArrayList<Lesson> lessons;
    ClassProcessAdapter classProcessAdapter;
    GridLayoutManager gridLayoutManager;
    DatabaseReference mData;

    private ClassProcessAdapter.RecyclerViewClickListener listener;

    public TeacherUpdateProcessFragment() {
        // Required empty public constructor
    }

    public static TeacherUpdateProcessFragment newInstance(Class aClass) {
        TeacherUpdateProcessFragment fragment = new TeacherUpdateProcessFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS, (Serializable) aClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            aClass = (Class) getArguments().getSerializable(ARG_CLASS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_update_process, container, false);

        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    private void setOnClickListener() {
        listener = new ClassProcessAdapter.RecyclerViewClickListener() {

            @Override
            public void onTouch(View v, int adapterPosition) {
                if(adapterPosition >= 0) {
                    Lesson lesson = lessons.get(adapterPosition);
                    LessonDAO.getInstance().changeStatusLesson(lesson, Common.semester.getSemesterId());
                }
            }

            @Override
            public void onCLick(View itemView, int adapterPosition) {
                Lesson lesson = lessons.get(adapterPosition);
                openDiagLog(lesson);
            }
        };
    }

    private void setRecyclerView(View root) {

        // Initialize
        lessons = new ArrayList<>();
        classProcessAdapter = new ClassProcessAdapter(getContext(), (ArrayList<Lesson>) lessons, listener);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

        // Set adapter for recycler view
        recyclerView = root.findViewById(R.id.rvListLessonProcess);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(classProcessAdapter);

        // Fill data from Firebase
        mData = FirebaseDatabase.getInstance().getReference("Lessons").child(Common.semester.getSemesterId());
        Query query = mData.orderByChild("classId").equalTo(aClass.getClassId());
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

    private void openDiagLog(Lesson lesson) {
        // Create and set some attributes for dialog
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enter_lesson);
        dialog.setCancelable(false);

        // Get dialog window and set some attributes for window
        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Mapping variables with view in layout
        TextView tvWeek = dialog.findViewById(R.id.txtWeek);
        EditText edLessonName = dialog.findViewById(R.id.edLessonName);
        EditText edLessonDescription = dialog.findViewById(R.id.edLessonDescription);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        // Set text
        tvWeek.setText(String.valueOf(lesson.getWeek()));
        edLessonName.setText(lesson.getName());
        edLessonDescription.setText(lesson.getDescription());

        // Dismiss dialog when click btnClose
        btnClose.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Save mark when click btnSave
        btnSave.setOnClickListener((View v) -> {
            try {
                lesson.setName(edLessonName.getText().toString());
                lesson.setDescription(edLessonDescription.getText().toString());
                LessonDAO.getInstance().update(lesson);
                Toast.makeText(getContext(), "Cập bài học thành công", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi. Vui lòng thử lại", Toast.LENGTH_LONG).show();
            }
        });

        // Show dialog
        dialog.show();
    }
}