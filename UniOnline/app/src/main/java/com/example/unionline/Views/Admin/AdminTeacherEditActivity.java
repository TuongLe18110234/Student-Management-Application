package com.example.unionline.Views.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.unionline.R;

public class AdminTeacherEditActivity extends AppCompatActivity {

    EditText et_username_edit_teacher, et_fullname_edit_teacher, et_email_edit_teacher, et_phone_edit_teacher, et_major_edit_teacher;
    Button btn_update_teacher;
    RadioButton rb_teacher_gender_male, rb_teacher_gender_female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_edit);

        btn_update_teacher = findViewById(R.id.btn_admin_edit_giangvien);
        rb_teacher_gender_male = findViewById(R.id.rb_admin_teacher_edit_gender_male);
        rb_teacher_gender_female = findViewById(R.id.rb_admin_teacher_edit_gender_female);
        et_username_edit_teacher = findViewById(R.id.et_admin_teacher_edit_username);
        et_fullname_edit_teacher = findViewById(R.id.et_admin_teacher_edit_fullname);
        et_phone_edit_teacher = findViewById(R.id.et_admin_teacher_edit_phone);
        et_email_edit_teacher = findViewById(R.id.et_admin_teacher_edit_email);
        et_major_edit_teacher = findViewById(R.id.et_admin_teacher_edit_major);

    }
}