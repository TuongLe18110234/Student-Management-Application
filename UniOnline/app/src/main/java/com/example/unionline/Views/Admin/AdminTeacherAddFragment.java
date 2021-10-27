package com.example.unionline.Views.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.unionline.Common;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;


public class AdminTeacherAddFragment extends DialogFragment {

    EditText et_name_teacher, et_phone_teacher, et_mail_teacher, et_major_teacher;
    Button btn_add_teacher;
    RadioButton rb_teacher_male, rb_teacher_female;
    private FirebaseAuth firebaseAuth;

    public static AdminTeacherAddFragment newInstance(String param1, String param2) {
        AdminTeacherAddFragment fragment = new AdminTeacherAddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_teacher_add, container, false);

        btn_add_teacher = view.findViewById(R.id.btn_admin_add_giangvien);
        et_name_teacher = view.findViewById(R.id.et_admin_teacher_add_fullname);
        et_mail_teacher = view.findViewById(R.id.et_admin_teacher_add_email);
        et_phone_teacher = view.findViewById(R.id.et_admin_teacher_add_phone);
        rb_teacher_male = view.findViewById(R.id.rb_admin_teacher_add_gender_male);
        rb_teacher_female = view.findViewById(R.id.rb_admin_teacher_add_gender_female);

        btn_add_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherEmail = et_mail_teacher.getText().toString().trim();
                String teacherDefaultPassword_Phone = "12345678";
                String teacherGender = null;

                if(rb_teacher_male.isChecked()){
                    teacherGender = "male";
                }
                if (rb_teacher_female.isChecked()){
                    teacherGender = "female";
                }

                if(et_mail_teacher.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in al the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_name_teacher.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in al the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_phone_teacher.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in al the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!emailCheck()){
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String finalTeacherGender = teacherGender;
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(teacherEmail, teacherDefaultPassword_Phone)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String teacherID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    User teacherAdd = new User(teacherID, et_name_teacher.getText().toString(), et_mail_teacher.getText().toString(), et_phone_teacher.getText().toString(), null, true, Common.roleTeacher, null, finalTeacherGender);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(teacherID).setValue(teacherAdd)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "User was added Successfully", Toast.LENGTH_LONG).show();
                                                        dismiss();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed to add! PLease try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                    return;
                                                }
                                            });
                                }
                                else {
                                    Toast.makeText(getActivity(), "Existed Email!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        });
                }
            }
        });

        return view;
    }
    boolean emailCheck (){
        String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        if (et_mail_teacher.getText().toString().trim().matches(emailPattern)) {
            return true;
        }
        else
        {
            return false;
        }
    }
}