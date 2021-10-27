package com.example.unionline.Views.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Adapters.Students.AbsenceApplicationAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;
import com.example.unionline.Views.Teachers.TeacherAddNotificationActivity;
import com.example.unionline.Views.Teachers.TeacherListNotificationsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class StudentAbsenceApplicationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<AbsenceApplication> absenceApplications;
    AbsenceApplicationAdapter adapter;
    DatabaseReference mDatabase;

    ImageView backIcon;
    TextView tvActivityName;
    private FloatingActionButton fabAddNAA;

    ArrayList<Enrollment> enrollments;
    ArrayList<String> classNames;

    private AbsenceApplicationAdapter.RecyclerViewClickListener listener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_absence_application);

        // Set activity name on toolbar
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText("Tạo đơn xin nghỉ");

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Button add abcence application
        fabAddNAA = (FloatingActionButton) findViewById(R.id.fabAddNAA);
        fabAddNAA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }
        });

        // Set event click for item in list absenceApplications
        setOnClickListener();
        setRecyclerView();
        getListClasses();
    }
    private void getListClasses() {
        enrollments = new ArrayList<Enrollment>();
        classNames = new ArrayList<String>();

        // Fill data from Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Enrollments").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("studentId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrollments.clear();
                classNames.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    enrollments.add(enrollment);
                    classNames.add(enrollment.getClassName());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Dialog to add absenceApplications
     */
    private void openAddDialog() {
        EditText edtContent;
        Spinner spClassNames;
        TextView tvDateOff;
        ImageView ivClose;
        Button btSave;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_absence_application, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);

        // Map view
        edtContent = view.findViewById(R.id.edtContent);
        spClassNames = view.findViewById(R.id.spClassNames);
        tvDateOff = view.findViewById(R.id.tvDateOff);
        ivClose = view.findViewById(R.id.iv_close);
        btSave = view.findViewById(R.id.btSave);

        // Set date off is choossed date
        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.format("dd/MM/yyyy", calendar).toString();
        tvDateOff.setText(date);

        // Set list class name to spiner class names
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClassNames.setAdapter(arrayAdapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        tvDateOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar(tvDateOff);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spClassNames.indexOfChild(spClassNames.get)
                Enrollment enrollment = enrollments.get(spClassNames.getSelectedItemPosition());
                String content = edtContent.getText().toString();

                if(content.trim().isEmpty()){
                    edtContent.setError("Không được để trống nội dung!");
                    edtContent.requestFocus();
                    return;
                }

                if(spClassNames.getSelectedItem() == null){
                    edtContent.setError("Chọn lớp để gửi đơn!");
                    edtContent.requestFocus();
                }

                // Add absence application
                mDatabase = FirebaseDatabase.getInstance().getReference().child("AbsenceApplications").child(Common.semester.getSemesterId());
                String key = mDatabase.push().getKey();
                String dateOff = tvDateOff.getText().toString();

                // Create Absence Application with data
                AbsenceApplication aa = new AbsenceApplication(key, enrollment.getClassId(), enrollment.getClassName(),
                        enrollment.getFullDate(), Common.user.getUserId(), Common.user.getName(),content, dateOff, Common.AA_WAIT_FOR_APPROVAL);

                mDatabase.child(aa.getId()).setValue(aa);
                alertDialog.cancel();
                Toast.makeText(StudentAbsenceApplicationActivity.this, "Thêm đơn thành công!", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }
    // Open calendar to select date off
    private void openCalendar(TextView tvDate) {
        tvDate.setError(null);

        // Create current date
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar_date = Calendar.getInstance();
                calendar_date.set(year,month,dayOfMonth);

                String date = DateFormat.format("dd/MM/yyyy", calendar_date).toString();

                // Check date is after date before
                if(calendar_date.before(calendar)){
                    tvDate.setError("Ngày đã qua");
                    tvDate.requestFocus();
                } else {
                    tvDate.setText(date);
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    /**
     * Update dialog to update abcence dialog
     */
    private void openUpdateDialog(AbsenceApplication aa) {
        EditText edtContent;
        ImageView ivClose;
        Button btSave;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_update_absence_application, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);

        edtContent = view.findViewById(R.id.edtContent);
        edtContent.setText(aa.getReason());

        ivClose = view.findViewById(R.id.iv_close);
        btSave = view.findViewById(R.id.btSave);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString();

                if(content.trim().isEmpty()){
                    edtContent.setError("Không được để trống nội dung");
                    return;
                }
                // Add absence application
                mDatabase = FirebaseDatabase.getInstance().getReference().child("AbsenceApplications").child(Common.semester.getSemesterId());

                aa.setReason(content);

                mDatabase.child(aa.getId()).setValue(aa);
                alertDialog.cancel();
                Toast.makeText(StudentAbsenceApplicationActivity.this, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }

    /**
     * Delete AbsenceApplication with is selected
     * @param aa
     */
    private void deleteAbsenceApplication(AbsenceApplication aa) {
        mDatabase = FirebaseDatabase.getInstance().getReference("AbsenceApplications");
        mDatabase.child(Common.semester.getSemesterId()).child(aa.getId()).removeValue();
        Toast.makeText(StudentAbsenceApplicationActivity.this, "Xóa đơn thành công!", Toast.LENGTH_LONG).show();

    }

    /**
     * Set onclick for recycelview item
     */
    private void setOnClickListener() {
        listener = new AbsenceApplicationAdapter.RecyclerViewClickListener() {
            @Override
            public void onCLick(View v, int position) {
                // Open application detail
                Intent intent = new Intent(StudentAbsenceApplicationActivity.this, StudentAbcenceAppDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("absenceApplication", (Serializable) absenceApplications.get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                // Add item to context menu
                menu.add(position,0,0,"Cập nhật lý do xin nghỉ");
                menu.add(position,1,1,"Xóa đơn");
            }
        };
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        AbsenceApplication aa = absenceApplications.get(position);
        switch (item.getItemId()){
            case 0:
                openUpdateDialog(aa);
                return true;
            case 1:
                deleteAbsenceApplication(aa);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void setRecyclerView() {
        // Initialize
        recyclerView = findViewById(R.id.rvAbsenceApplication);

        absenceApplications  = new ArrayList<>();

        adapter = new AbsenceApplicationAdapter(this, absenceApplications, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set adapter for recycler view
        recyclerView.setAdapter(adapter);

        // Fill data from Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("AbsenceApplications").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("studentId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                absenceApplications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AbsenceApplication absenceApplication = dataSnapshot.getValue(AbsenceApplication.class);
                    absenceApplications.add(absenceApplication);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}