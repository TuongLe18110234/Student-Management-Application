package com.example.unionline.Views.Teachers.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Adapters.Teachers.ApplicationAdapter;
import com.example.unionline.Common;
import com.example.unionline.DAO.AbsenceApplicationDAO;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TeacherPendingApplicationFragment extends Fragment {

    private static final String ARG_CLASS_IDS = "classIds";
    private ArrayList<String> classIds;

    RecyclerView recyclerView;
    List<AbsenceApplication> applications;
    ApplicationAdapter adapter;
    GridLayoutManager gridLayoutManager;
    DatabaseReference mData;

    private ApplicationAdapter.RecyclerViewClickListener listener;
    private TextView tvStudentName, tvStudentId, tvDateOff, tvReason;
    private RadioButton rbApprove, rbRefuse;
    private Button btnSend;
    private EditText edResponse;
    Dialog dialog;

    public TeacherPendingApplicationFragment() {
        // Required empty public constructor
    }

    public static TeacherPendingApplicationFragment newInstance(ArrayList<String> classIds) {
        TeacherPendingApplicationFragment fragment = new TeacherPendingApplicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS_IDS, classIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classIds = (ArrayList<String>) getArguments().getSerializable(ARG_CLASS_IDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_application, container, false);

        setDialog();
        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    private void setOnClickListener() {
        listener = new ApplicationAdapter.RecyclerViewClickListener() {

            @Override
            public void onTouch(View v, int position) {

            }

            @Override
            public void onCLick(View itemView, int adapterPosition) {
                AbsenceApplication application = applications.get(adapterPosition);
                openDialog(application);
            }
        };
    }

    private void setRecyclerView(View root) {

        // Initialize
        applications = new ArrayList<>();

        adapter = new ApplicationAdapter(getContext(), (ArrayList<AbsenceApplication>) applications, listener);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);

        // Set adapter for recycler view
        recyclerView = root.findViewById(R.id.rvAbsenceApplication);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        // Fill data from Firebase
        mData = FirebaseDatabase.getInstance().getReference("AbsenceApplications").child(Common.semester.getSemesterId());
        Query query = mData.orderByChild("state").equalTo(Common.AA_WAIT_FOR_APPROVAL);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                applications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AbsenceApplication application = dataSnapshot.getValue(AbsenceApplication.class);

                    // Application in list classes that teacher teaches and state of application is waiting
                    if(classIds.contains(application.getClassId())) {
                        applications.add(application);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }


        });
    }

    private void setDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_application, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        dialog.setContentView(view);

        tvStudentId = dialog.findViewById(R.id.txtStudentIdValue);
        tvStudentName = dialog.findViewById(R.id.txtStudentNameValue);
        tvDateOff = dialog.findViewById(R.id.txtDateOffValue);
        tvReason = dialog.findViewById(R.id.txtReason);
        rbApprove = dialog.findViewById(R.id.rbApprove);
        rbRefuse = dialog.findViewById(R.id.rbRefuse);
        edResponse = dialog.findViewById(R.id.txtRespond);

        btnSend = dialog.findViewById(R.id.btnSend);

        setToolbar();
    }

    private void setToolbar() {
        ImageView backIcon = dialog.findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        TextView txtToolbarName = dialog.findViewById(R.id.activity_name);
        txtToolbarName.setText("Duyệt đơn xin nghỉ");
    }

    private void openDialog(AbsenceApplication application) {
        tvStudentId.setText(application.getStudentId());
        tvStudentName.setText(application.getStudentName());
        tvReason.setText(application.getReason());
        tvDateOff.setText(application.getDateOff());
        edResponse.setText(application.getTeacherRespond());


        switch (application.getState()) {
            case Common.AA_APPROVED:
                rbApprove.setChecked(true);
                break;
            case Common.AA_NOT_APPROVED:
                rbRefuse.setChecked(true);
                break;
            default:
                rbApprove.setChecked(false);
                rbRefuse.setChecked(false);
                break;
        }

        btnSend.setOnClickListener((View v) -> {
            if(rbApprove.isChecked()) {
                application.setState(Common.AA_APPROVED);
            } else if(rbRefuse.isChecked()) {
                application.setState(Common.AA_NOT_APPROVED);
            } else {
                application.setState(Common.AA_WAIT_FOR_APPROVAL);
            }

            application.setTeacherRespond(edResponse.getText().toString());

            try {
                AbsenceApplicationDAO.getInstance().update(application);
                Toast.makeText(getContext(), "Đã duyệt đơn", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}