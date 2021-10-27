package com.example.unionline.Views.Admin;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class AdminQlkAddFragment extends DialogFragment {

    EditText et_name_qlk, et_phone_qlk, et_mail_qlk;
    Button btn_add_qlk;

    private FirebaseAuth firebaseAuth;

    public static AdminQlkAddFragment newInstance(String param1, String param2) {
        AdminQlkAddFragment fragment = new AdminQlkAddFragment();
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

        View view = inflater.inflate(R.layout.fragment_admin_qlk_add, container, false);

        et_name_qlk = view.findViewById(R.id.et_admin_qlkhoa_add_fullname);
        et_phone_qlk = view.findViewById(R.id.et_admin_qlkhoa_add_phone);
        et_mail_qlk = view.findViewById(R.id.et_admin_qlkhoa_add_email);
        btn_add_qlk = view.findViewById(R.id.btn_admin_add_qlk);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_add_qlk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qlkEmail = et_mail_qlk.getText().toString().trim();
                String qlkDefaultPassword_Phone = "12345678";

                if(et_mail_qlk.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_name_qlk.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(et_phone_qlk.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all the field!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailCheck() == false){
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(qlkEmail, qlkDefaultPassword_Phone)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String qlkID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                User qlkAdd = new User(qlkID, et_name_qlk.getText().toString(), et_mail_qlk.getText().toString(), et_phone_qlk.getText().toString(), null, true, Common.roleManager, null, null);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(qlkID).setValue(qlkAdd)
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
        if (et_mail_qlk.getText().toString().trim().matches(emailPattern)) {
            return true;
        }
        else
        {
            return false;
        }
    }
}