package com.example.unionline.Views.Manager;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//import com.example.implementproject.controllers.ManageClassControllers;
//import com.example.implementproject.model.AbsenceApplication;
//import com.example.implementproject.model.Attendance;
//import com.example.implementproject.model.ClassModel1;
//import com.example.implementproject.model.Enrollment;
//import com.example.implementproject.model.Lesson;
//import com.example.implementproject.model.Notification;
//import com.example.implementproject.model.Parent_Student;
//import com.example.implementproject.model.Semester;
//import com.example.implementproject.model.User;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.ClassModel1;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.DTO.Parent_Student;
import com.example.unionline.DTO.Semester;
import com.example.unionline.DTO.User;
import com.example.unionline.Views.Common.CommonAccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.unionline.R;
public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.manager_container,new ManageClassControllers());
        fragmentTransaction.commit();
        bottomNav = findViewById(R.id.manager_bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("HoTen").setValue("Nguyen Duong Dat");
        //mDatabase.child("Classes").child("OPP2021").setValue("classModel");
        //addDataV2();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = (@NonNull MenuItem item) -> {
        Fragment selectedFragment = null;

        // Set selected fragment by corresponding fragment
        switch (item.getItemId()) {
            case R.id.nav_manager_home:
                selectedFragment = new ManageClassControllers();
                break;
            case R.id.nav_manager_notification:

                selectedFragment = new ManageNotificationControllers();
                break;
            case R.id.nav_manager_account:
                selectedFragment = new CommonAccountFragment();
                break;

        }
        // Replace fragment container with selected fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.manager_container, selectedFragment).commit();
        return true;
    };
    private void addDataV2(){
        DatabaseReference mDatabase;
        String key;

        Semester semester = new Semester();
        semester.setSemesterId("2020_2021_HK1");
        semester.setSemesterName("Hoc ky 1, nam hoc 2020-2021");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Semesters").child(semester.getSemesterId()).setValue(semester);

        //Add user
        User user = new User();
        user.setUserId("18110234");
        user.setEmail("Le Nhat Tuong");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getUserId()).setValue(user);

        Parent_Student parent_student = new Parent_Student("10110101","18110234");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Parent_Students");
        key = mDatabase.push().getKey();
        parent_student.setId(key);
        mDatabase.child(key).setValue(parent_student);

        ClassModel1 class_s = new ClassModel1();
        class_s.setTeacherId(user.getUserId());
        class_s.setSemesterId(semester.getSemesterId());
        class_s.setClassName("Lập trình di động");
        class_s.setCapacity(40);
        mDatabase = FirebaseDatabase.getInstance().getReference("Classes").child(semester.getSemesterId());
        key = mDatabase.push().getKey();
        class_s.setClassId(key);
        mDatabase.child(class_s.getClassId()).setValue(class_s);

        //Add enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setClassId(class_s.getClassId());
        enrollment.setStudentId(user.getUserId());
        enrollment.setStudentName(user.getName());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Enrollments").child(semester.getSemesterId());
        key = mDatabase.push().getKey();
        enrollment.setId(key);
        mDatabase.child(key).setValue(enrollment);

        //Add lesson
//        Lesson lesson = new Lesson();
//        lesson.setLessonId("1");
//        lesson.setName("Web API");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Lessons").child(semester.getSemesterId());
        key = mDatabase.push().getKey();
//        lesson.setClassId(key);
//        mDatabase.child(key).setValue(lesson);

        // Add attendance
        Attendance attendance = new Attendance();
        attendance.setClassId(class_s.getClassId());
//        attendance.setLessonId(lesson.getLessonId());
        attendance.setStudentId(user.getUserId());
        attendance.setState(1);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Attendances").child(semester.getSemesterId());
        key = mDatabase.push().getKey();
        attendance.setId(key);
        mDatabase.child(key).setValue(attendance);

        // Add absence application
        AbsenceApplication aa = new AbsenceApplication();
        aa.setClassId(class_s.getClassId());
        aa.setStudentId(user.getUserId());
        aa.setReason("So late");
        aa.setState(0);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("AbsenceApplications").child(semester.getSemesterId());
        key = mDatabase.push().getKey();
        aa.setId(key);
        mDatabase.setValue(aa);

//        Notification notification = new Notification("1", "1", "1", R.drawable.logo_hcmute, "1", "1");
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("Notifications").child(String.valueOf(notification.getId())).setValue(notification);
    }
}