package com.example.unionline.Views.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.unionline.R;

public class AdminQlkEditActivity extends AppCompatActivity {

    ImageButton btn_back_edit_qlk;
    Button btn_edit_qlk;
    EditText et_username_edit_qlk, et_fullname_edit_qlk, et_phone_edit_qlk, et_mail_edit_qlk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_qlk_edit);

        btn_back_edit_qlk = findViewById(R.id.btn_back_edit_quanly);
        btn_edit_qlk = findViewById(R.id.btn_admin_edit_qlkhoa);
        et_username_edit_qlk = findViewById(R.id.et_admin_qlkhoa_edit_username);
        et_fullname_edit_qlk = findViewById(R.id.et_admin_qlkhoa_edit_fullname);
        et_phone_edit_qlk = findViewById(R.id.et_admin_qlkhoa_edit_phone);
        et_mail_edit_qlk = findViewById(R.id.et_admin_qlkhoa_edit_email);

    }
}