package com.example.unionline.Views.Students;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Common;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

public class StudentAbcenceAppDetailActivity extends AppCompatActivity {

    TextView tvStudentName, tvCreateDate, tvReasonAbsent, tvClassName, tvClassTime,
            tvAbsentState, tvDateOff, tvTeacherRespond;

    AbsenceApplication absenceApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_abcence_app_detail);

        setToolBar("Chi tiết đơn xin nghỉ");

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            absenceApplication = (AbsenceApplication) bundle.getSerializable("absenceApplication");
        }

        mapView();
        setView();
    }

    /**
     * Set tool bar for activity
     * @param name
     */
    private void setToolBar(String name){
        // Set activity name on toolbar
        TextView tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText(name);

        // Set event click for backIcon on toolbar
        // When click backIcon: finish this activity
        ImageView backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });
    }

    private void mapView(){
        tvStudentName = findViewById(R.id.tvStudentName);
        tvCreateDate = findViewById(R.id.tvCreateDate);
        tvReasonAbsent = findViewById(R.id.tvReasonAbsent);
        tvClassName = findViewById(R.id.tvClassName);
        tvClassTime = findViewById(R.id.tvClassTime);
        tvAbsentState = findViewById(R.id.tvAbsentState);
        tvDateOff = findViewById(R.id.tvDateOff);
        tvTeacherRespond = findViewById(R.id.tvTeacherRespond);
    }

    private void setView() {
        if(absenceApplication!=null){
            tvStudentName.setText(absenceApplication.getStudentName());
            tvCreateDate.setText(absenceApplication.getDateCreate());
            tvReasonAbsent.setText(absenceApplication.getReason());
            tvClassName.setText(absenceApplication.getClassName());
            tvClassTime.setText(absenceApplication.getClassTime());
            tvAbsentState.setText(Common.aaNames.get(absenceApplication.getState()));
            tvDateOff.setText(absenceApplication.getDateOff());

            if (absenceApplication.getTeacherRespond() != null){
                tvTeacherRespond.setText(absenceApplication.getTeacherRespond());
            } else {
                tvTeacherRespond.setText("Chưa có phản hồi");
            }
        }
    }
}