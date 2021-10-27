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

import com.example.unionline.Adapters.Teachers.ClassMarkAdapter;
import com.example.unionline.Common;
import com.example.unionline.DAO.EnrollmentDAO;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Enrollment;
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

public class TeacherMarkFragment extends Fragment {

    private static final String ARG_CLASS = "class";
    private Class aClass;

    RecyclerView recyclerView;
    List<Enrollment> scores;
    ClassMarkAdapter classMarkAdapter;
    GridLayoutManager gridLayoutManager;
    DatabaseReference mData;

    private ClassMarkAdapter.RecyclerViewClickListener listener;

    public TeacherMarkFragment() {
        // Required empty public constructor
    }


    public static TeacherMarkFragment newInstance(Class aClass) {
        TeacherMarkFragment fragment = new TeacherMarkFragment();
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
        View root = inflater.inflate(R.layout.fragment_teacher_mark, container, false);

        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    private void setOnClickListener() {
        listener = new ClassMarkAdapter.RecyclerViewClickListener() {

            @Override
            public void onTouch(View v, int position) {

            }

            @Override
            public void onCLick(View itemView, int adapterPosition) {
                Enrollment score = scores.get(adapterPosition);
                openDiagLog(score);
            }
        };
    }

    private void setRecyclerView(View root) {

        // Initialize
        scores = new ArrayList<>();

        classMarkAdapter = new ClassMarkAdapter(getContext(), (ArrayList<Enrollment>) scores, listener);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

        // Set adapter for recycler view
        recyclerView = root.findViewById(R.id.rvListScore);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(classMarkAdapter);

        // Fill data from Firebase
        // Get enrollments in selected class
        mData = FirebaseDatabase.getInstance().getReference("Enrollments").child(Common.semester.getSemesterId());
        Query query = mData.orderByChild("classId").equalTo(aClass.getClassId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                scores.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    scores.add(enrollment);
                }
                classMarkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void openDiagLog(Enrollment score) {
        // Create and set some attributes for dialog
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diaglog_enter_mark);
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
        TextView tvStudentName = dialog.findViewById(R.id.txtStudentName);
        TextView tvStudentId = dialog.findViewById(R.id.txtStudentId);
        EditText edMidTerm = dialog.findViewById(R.id.edMidTerm);
        EditText edFinal = dialog.findViewById(R.id.edFinal);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        // Set text
        tvStudentName.setText(score.getStudentName());
        tvStudentId.setText(score.getStudentCode());

        if(score.getMidScore() != -1) {
            edMidTerm.setText(String.valueOf(score.getMidScore()));
        } else {
            edMidTerm.setText("");
        }

        if(score.getFinalScore() != -1) {
            edFinal.setText(String.valueOf(score.getFinalScore()));
        } else {
            edFinal.setText("");
        }

        // Dismiss dialog when click btnClose
        btnClose.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Save mark when click btnSave
        btnSave.setOnClickListener((View v) -> {
            try {
                String midScoreStr = edMidTerm.getText().toString();
                String finalScoreStr = edFinal.getText().toString();

                if(!midScoreStr.equals("")){
                    score.setMidScore(Double.parseDouble(midScoreStr));
                } else {
                    score.setMidScore(-1);
                }

                if(!finalScoreStr.equals("")){
                    score.setFinalScore(Double.parseDouble(finalScoreStr));
                } else {
                    score.setFinalScore(-1);
                }

                if((!midScoreStr.equals("")) && (!finalScoreStr.equals(""))){
                    score.setStateMark(1);
                } else {
                    score.setStateMark(0);
                }

                EnrollmentDAO.getInstance().update(score);
                Toast.makeText(getContext(), "Cập nhật điểm thành công", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi. Vui lòng thử lại", Toast.LENGTH_LONG).show();
            }
        });

        // Show dialog
        dialog.show();
    }
}