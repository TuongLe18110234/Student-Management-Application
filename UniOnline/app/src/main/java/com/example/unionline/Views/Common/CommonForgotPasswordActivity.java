package com.example.unionline.Views.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.unionline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CommonForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btResetPassword;
    ImageView backIcon;

    FirebaseAuth mAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_forgot_password);
        
        etEmail = (EditText)findViewById(R.id.etEmail);

        // get and set button reset password
        btResetPassword = (Button) findViewById(R.id.btResetPassword);
        btResetPassword.setOnClickListener((View v) -> {
            resetPassword();
        });

        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });
    }

    /**
     * Reset password
     */
    private void resetPassword() {
        String email = etEmail.getText().toString().trim();

        // Validate email
        if(!doValidate(email)){
            return;
        }

        // Reset password
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CommonForgotPasswordActivity.this, "Kiểm tra Email để cập nhật mật khẩu!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(CommonForgotPasswordActivity.this, "Tài khoản không tồn tại trong hệ thống!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Validate email
     * @param email
     * @return true if email is correct
     */
    private boolean doValidate(String email) {
        if (email.isEmpty()){
            etEmail.setError("Chưa nhập Email!");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không đúng!");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }
}