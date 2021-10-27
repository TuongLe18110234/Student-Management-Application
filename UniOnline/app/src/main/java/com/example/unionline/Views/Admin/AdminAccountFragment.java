package com.example.unionline.Views.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.unionline.R;


public class AdminAccountFragment extends Fragment {

    Button btnQlkAccount, btnTeacherAccount, btnStudentAccount, btnParentAccount;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_account, container, false);
        context = rootView.getContext();

        btnQlkAccount = rootView.findViewById(R.id.btn_account_qlkhoa);
        btnTeacherAccount = rootView.findViewById(R.id.btn_account_giangvien);
        btnStudentAccount = rootView.findViewById(R.id.btn_account_sinhvien);
        btnParentAccount = rootView.findViewById(R.id.btn_account_phuhuynh);

        btnQlkAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminQlkAccountActivity.class);
                startActivity(intent);
            }
        });

        btnTeacherAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminTeacherAccountActivity.class);
                startActivity(intent);
            }
        });

        btnStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminStudentAccountActivity.class);
                startActivity(intent);
            }
        });

        btnParentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminParentAccountActivity.class);
                startActivity(intent);
            }
        });

        return rootView;

    }
}