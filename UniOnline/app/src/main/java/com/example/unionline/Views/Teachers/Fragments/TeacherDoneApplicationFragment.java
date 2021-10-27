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
import com.example.unionline.Adapters.Teachers.ApplicationPagerAdapter;
import com.example.unionline.Adapters.Teachers.ClassMarkAdapter;
import com.example.unionline.Common;
import com.example.unionline.DAO.AbsenceApplicationDAO;
import com.example.unionline.DTO.AbsenceApplication;
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

public class TeacherDoneApplicationFragment extends Fragment {

    private static final String ARG_CLASS_IDS = "classIds";
    private ArrayList<String> classIds;

    RecyclerView recyclerView;
    List<AbsenceApplication> applications;
    ApplicationAdapter adapter;
    GridLayoutManager gridLayoutManager;
    DatabaseReference mData;

    private ApplicationAdapter.RecyclerViewClickListener listener;

    Dialog dialog;
    private TextView tvStudentName, tvStudentId, tvDateOff, tvReason;
    private RadioButton rbApprove, rbRefuse;
    private EditText edResponse;
    private Button btnSend;

    public TeacherDoneApplicationFragment() {
        // Required empty public constructor
    }

    public static TeacherDoneApplicationFragment newInstance(ArrayList<String> classIds) {
        TeacherDoneApplicationFragment fragment = new TeacherDoneApplicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS_IDS, (Serializable) classIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Get list class ids from bundle
            classIds = (ArrayList<String>) getArguments().getSerializable(ARG_CLASS_IDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_application, container, false);

        // Full screen dialog to approve the application
        setDialog();

        // Catch event when click or touch on item in list application
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
                // Get selected application
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
        // Get list waiting applications of classes that teacher teaches
        mData = FirebaseDatabase.getInstance().getReference("AbsenceApplications").child(Common.semester.getSemesterId());
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                applications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AbsenceApplication application = dataSnapshot.getValue(AbsenceApplication.class);
                    // Condition to add the application
                    // Class Id of the application must be in classIds and state of application is waiting
                    // classIds is list id of class that teacher is teaching
                    if((classIds.contains(application.getClassId())) && (application.getState() != Common.AA_WAIT_FOR_APPROVAL)) {
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
        // Create view of full screen layout
        View view = getLayoutInflater().inflate(R.layout.dialog_application, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        dialog.setContentView(view);

        // Mapping
        tvStudentId = dialog.findViewById(R.id.txtStudentIdValue);
        tvStudentName = dialog.findViewById(R.id.txtStudentNameValue);
        tvDateOff = dialog.findViewById(R.id.txtDateOffValue);
        tvReason = dialog.findViewById(R.id.txtReason);
        rbApprove = dialog.findViewById(R.id.rbApprove);
        rbRefuse = dialog.findViewById(R.id.rbRefuse);
        btnSend = dialog.findViewById(R.id.btnSend);
        edResponse = dialog.findViewById(R.id.txtRespond);

        // Toolbar on top of the dialog
        setToolbar();
    }

    private void setToolbar() {
        // Dismiss the dialog when clicking on back icon
        ImageView backIcon = dialog.findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Name of the dialog show on toolbar
        TextView txtToolbarName = dialog.findViewById(R.id.activity_name);
        txtToolbarName.setText("Duyệt đơn xin nghỉ");
    }

    /**
     * Open dialog when user clicking on any applications in recycler view
     * @param application: selected application
     */
    private void openDialog(AbsenceApplication application) {
        // Set value for view with data from application
        tvStudentId.setText(application.getStudentId());
        tvStudentName.setText(application.getStudentName());
        tvReason.setText(application.getReason());
        tvDateOff.setText(application.getDateOff());

        // Set check for radio buttons
        switch (application.getState()) {
            // Application is approved
            case Common.AA_APPROVED:
                rbApprove.setChecked(true);
                break;
            // Application is not approved
            case Common.AA_NOT_APPROVED:
                rbRefuse.setChecked(true);
                break;
            // Application has never been checked
            default:
                rbApprove.setChecked(false);
                rbRefuse.setChecked(false);
                break;
        }

        btnSend.setOnClickListener((View v) -> {
            // Change state for application when teacher check on radio buttons
            if(rbApprove.isChecked()) {
                // Approved
                application.setState(Common.AA_APPROVED);
            }
            else if(rbRefuse.isChecked()) {
                // Refused
                application.setState(Common.AA_NOT_APPROVED);
            }
            else {
                // Waiting
                application.setState(Common.AA_WAIT_FOR_APPROVAL);
            }

            // Response from teacher
            application.setTeacherRespond(edResponse.getText().toString());

            // Update to databse
            try {
                // If success
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