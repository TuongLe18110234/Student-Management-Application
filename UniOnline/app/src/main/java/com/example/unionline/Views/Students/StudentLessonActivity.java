package com.example.unionline.Views.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Common;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentLessonActivity extends AppCompatActivity {

    TextView tvActivityName, tvClassName, tvRoom, tvState, tvFullTime, tvAttendanceState,
            tvTeacherName, tvLessonName, tvLessonDescription;

    ImageView backIcon, ivTeacherAvatar;

    Lesson lesson;

    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_lesson);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            lesson = (Lesson) bundle.getSerializable("lesson");
        }

        //Set event click for back icon
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        mapView();
        mapViewValue();
    }

    /**
     * Map view to variable
     */
    private void mapView(){
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvClassName = (TextView) findViewById(R.id.tvClassName);
        tvRoom = (TextView) findViewById(R.id.tvRoom);
        tvState = (TextView) findViewById(R.id.tvState);
        tvFullTime = (TextView) findViewById(R.id.tvClassDate);
        tvAttendanceState = (TextView) findViewById(R.id.tvAttendanceState);
        tvTeacherName = (TextView) findViewById(R.id.tvTeacherName);
        tvLessonName = (TextView) findViewById(R.id.tvLessonName);
        tvLessonDescription = (TextView) findViewById(R.id.tvLessonDescription);
        ivTeacherAvatar = (ImageView) findViewById(R.id.ivTeacherAvatar);
    }

    private void mapViewValue(){

        // Get class
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Classes").child(Common.semester.getSemesterId()).child(lesson.getClassId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    Class aClass = task.getResult().getValue(Class.class);

                    // Set class Name and class room
                    tvClassName.setText(aClass.getClassName());
                    tvRoom.setText(aClass.getRoom());
                    tvFullTime.setText(lesson.getDate()  + " | Tiết" + aClass.getStartTime() + " - " + aClass.getEndTime());

                    // Get teacher from id
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Users").child(aClass.getTeacherId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                            }
                            else {
                                User user = task.getResult().getValue(User.class);
                                if(user!=null){
                                    tvTeacherName.setText(user.getName());
                                }
                            }
                        }
                    });
                }
            }
        });

        // Set lesson name, lesson description
        tvLessonName.setText(lesson.getName());
        tvLessonDescription.setText(lesson.getDescription());
        tvActivityName.setText("Buổi học thứ " + lesson.getWeek());

        /**
         * Set lesson state
         */
        if(lesson.isStatus()) {
            tvState.setText("Đã học");
        } else {
            tvState.setText("Chưa học");
        }
    }
}