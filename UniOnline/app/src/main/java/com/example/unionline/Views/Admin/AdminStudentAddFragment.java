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
import com.example.unionline.DAO.AdminDAO;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AdminStudentAddFragment extends DialogFragment {

    EditText et_name_Student, et_phone_Student, et_mail_Student, et_address_Student, et_major_Student;
    Button btn_add_Student;
    RadioButton rb_student_gender_male, rb_student_gender_female;

    private FirebaseAuth firebaseAuth;

    public static AdminStudentAddFragment newInstance(String param1, String param2) {
        AdminStudentAddFragment fragment = new AdminStudentAddFragment();
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

        View view = inflater.inflate(R.layout.fragment_admin_student_add, container, false);

        et_name_Student = view.findViewById(R.id.et_admin_student_add_fullname);
        et_phone_Student = view.findViewById(R.id.et_admin_student_add_phone);
        et_mail_Student = view.findViewById(R.id.et_admin_student_add_email);
        et_address_Student = view.findViewById(R.id.et_admin_student_add_address);
        et_major_Student = view.findViewById(R.id.et_admin_student_add_major);
        btn_add_Student = view.findViewById(R.id.btn_add_new_student);
        rb_student_gender_male = view.findViewById(R.id.rb_admin_student_add_gender_male);
        rb_student_gender_female = view.findViewById(R.id.rb_admin_student_add_gender_female);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_add_Student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentEmail = et_mail_Student.getText().toString().trim();
                String studentDefaultPassword_Phone = "12345678";
                String studentGender = null;

                if(rb_student_gender_male.isChecked()){
                    studentGender = "male";
                }
                if (rb_student_gender_female.isChecked()){
                    studentGender = "female";
                }

                if(et_mail_Student.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_name_Student.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_phone_Student.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (et_major_Student.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (et_address_Student.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailCheck() == false){
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String finalStudentGender = studentGender;
                    firebaseAuth.createUserWithEmailAndPassword(studentEmail, studentDefaultPassword_Phone)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String studentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        User studentAdd = new User(studentID, et_name_Student.getText().toString(),
                                                et_mail_Student.getText().toString(), et_phone_Student.getText().toString(), et_major_Student.getText().toString(),
                                                true, Common.roleStudent, et_address_Student.getText().toString(), finalStudentGender.toString());
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(studentID).setValue(studentAdd)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "User was added successfully", Toast.LENGTH_LONG).show();
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
        if (et_mail_Student.getText().toString().trim().matches(emailPattern)) {
            return true;
        }
        else
        {
            return false;
        }
    }
}