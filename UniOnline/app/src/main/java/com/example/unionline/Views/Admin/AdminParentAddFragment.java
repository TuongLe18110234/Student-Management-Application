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
import android.widget.ImageButton;
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

public class AdminParentAddFragment extends DialogFragment {

    EditText  et_name_parent, et_phone_parent, et_mail_parent, et_address_parent;
    Button btn_add_parent;
    RadioButton rb_parent_gender_male, rb_parent_gender_female;
    private FirebaseAuth firebaseAuth;

    public static AdminParentAddFragment newInstance(String param1, String param2) {
        AdminParentAddFragment fragment = new AdminParentAddFragment();
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

        View view = inflater.inflate(R.layout.fragment_admin_parent_add, container, false);

        et_name_parent = view.findViewById(R.id.et_admin_parent_add_fullname);
        et_phone_parent = view.findViewById(R.id.et_admin_parent_add_phone);
        et_mail_parent = view.findViewById(R.id.et_admin_parent_add_email);
        et_address_parent = view.findViewById(R.id.et_admin_parent_add_address);
        btn_add_parent = view.findViewById(R.id.btn_admin_add_phuhuynh);
        rb_parent_gender_male = view.findViewById(R.id.rb_admin_parent_add_gender_male);
        rb_parent_gender_female = view.findViewById(R.id.rb_admin_parent_add_gender_female);

        btn_add_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String parentGender = null;
                String parentEmail = et_mail_parent.getText().toString().trim();
                String parentDefaultPassword_Phone = "12345678";

                if(rb_parent_gender_male.isChecked()){
                    parentGender = "male";
                }
                if (rb_parent_gender_female.isChecked()){
                    parentGender = "female";
                }

                if(et_mail_parent.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in al the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_name_parent.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in al the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_phone_parent.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in al the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!emailCheck()){
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String finalParentGender = parentGender;
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(parentEmail, parentDefaultPassword_Phone)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String parentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        User parentAdd = new User(parentID, et_name_parent.getText().toString(), et_mail_parent.getText().toString(), et_phone_parent.getText().toString(), null, true, Common.roleParent, null, finalParentGender);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(parentID).setValue(parentAdd)
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
        if (et_mail_parent.getText().toString().trim().matches(emailPattern)) {
            return true;
        }
        else
        {
            return false;
        }
    }
}