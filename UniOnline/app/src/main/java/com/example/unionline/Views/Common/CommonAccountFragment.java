package com.example.unionline.Views.Common;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Common;
import com.example.unionline.DAO.UserDAO;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class CommonAccountFragment extends Fragment {

    private static final int UPDATE_PROFILE = 1;
    private static final int CHANGE_PASSWORD = 2;

    Button btnUpdateProfile, btnChangePassword, btnLogOut;

    Dialog dialogUpdateProfile, dialogChangePassword;
    EditText edName, edPhone, edEmail, edCurrentPass, edNewPass, edReNewPass;
    Button btnUpdate, btnChange;

    private enum Output {
        valid,
        empty_password,
        empty_newPassword,
        invalid_newPassword,
        empty_reNewPassword,
        wrong_renewPassword
    }

    public CommonAccountFragment() {
        // Required empty public constructor
    }

    public static CommonAccountFragment newInstance() {
        CommonAccountFragment fragment = new CommonAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpdateProfileDialog();
        setChangePassworDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_account, container, false);

        btnUpdateProfile = root.findViewById(R.id.btnUpdateProfile);
        btnChangePassword = root.findViewById(R.id.btnChangePassword);
        btnLogOut = root.findViewById(R.id.btnLogOut);

        btnUpdateProfile.setOnClickListener((View v) -> {
            dialogUpdateProfile.show();
        });

        btnChangePassword.setOnClickListener((View v) -> {
            dialogChangePassword.show();
        });

        btnLogOut.setOnClickListener((View v) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this.getContext(), CommonLoginActivity.class));
            this.getActivity().finish();
        });

        return root;
    }

    private void setUpdateProfileDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_update_profile, null);
        dialogUpdateProfile = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        dialogUpdateProfile.setContentView(view);

        edName = dialogUpdateProfile.findViewById(R.id.edName);
        edPhone = dialogUpdateProfile.findViewById(R.id.edPhone);
        edEmail = dialogUpdateProfile.findViewById(R.id.edEmail);

        edName.setText(Common.user.getName());
        edPhone.setText(Common.user.getPhone());
        edEmail.setText(Common.user.getEmail());

        btnUpdate = dialogUpdateProfile.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener((View v) -> {
            String name = edName.getText().toString();
            if(name == "") {
                edName.setError("Nhập tên của bạn!");
            }

            String phone = edPhone.getText().toString();
            if(phone == "") {
                edPhone.setError("Nhập số điện thoại!D");
            }

            updateProfile(name, phone);
        });

        setToolbar(dialogUpdateProfile, UPDATE_PROFILE);
    }

    private void setChangePassworDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        dialogChangePassword = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        dialogChangePassword.setContentView(view);

        edCurrentPass = dialogChangePassword.findViewById(R.id.edCurrentPass);
        edNewPass = dialogChangePassword.findViewById(R.id.edNewPass);
        edReNewPass = dialogChangePassword.findViewById(R.id.edReNewPass);

        btnChange = dialogChangePassword.findViewById(R.id.btnChange);
        btnChange.setOnClickListener((View v) -> {
            String currentPass = edCurrentPass.getText().toString();
            String newPass = edNewPass.getText().toString();
            String reNewPass = edReNewPass.getText().toString();

            changePassword(currentPass, newPass, reNewPass);
        });

        setToolbar(dialogChangePassword, CHANGE_PASSWORD);
    }

    private void setToolbar(Dialog dialog, int Type) {
        ImageView backIcon = dialog.findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        TextView txtToolbarName = dialog.findViewById(R.id.activity_name);

        if(Type == UPDATE_PROFILE) {
            txtToolbarName.setText("Cập nhật hồ sơ cá nhân");
        } else if(Type == CHANGE_PASSWORD) {
            txtToolbarName.setText("Thay đổi mật khẩu");
        }
    }

    private void updateProfile(String name, String phone) {
        User user = Common.user;
        user.setName(name);
        user.setPhone(phone);

        try {
            UserDAO.getInstance().update(user);
            Common.user = user;
            Toast.makeText(getContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Đã xảy ra lỗi. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword(String currentPassword, String newPassword, String reNewPassword) {
        Output result = validateInput(currentPassword, newPassword, reNewPassword);

        switch (result){
            case valid:
                saveNewPassword(currentPassword, newPassword);
                break;
            case empty_password:
                edCurrentPass.setError("Nhập mật khẩu cũ!");
                edCurrentPass.requestFocus();
                break;
            case empty_newPassword:
                edNewPass.setError("Nhập mật khẩu mới!");
                edNewPass.requestFocus();
                break;
            case invalid_newPassword:
                edNewPass.setError("Mật khẩu phải có ít nhất 8 ký tự!");
                edNewPass.requestFocus();
                break;
            case empty_reNewPassword:
                edReNewPass.setError("Nhập lại mật khẩu mới!");
                edReNewPass.requestFocus();
                break;
            case wrong_renewPassword:
                edReNewPass.setError("Mật khẩu mới không khớp!");
                edReNewPass.requestFocus();
                break;
        }
    }

    private Output validateInput(String currentPassword, String newPassword, String reNewPassword){
        if(currentPassword.isEmpty()){
            return Output.empty_password;
        }

        if(newPassword.isEmpty()){
            return Output.empty_newPassword;
        }

        if(newPassword.length() < 8){
            return Output.invalid_newPassword;
        }

        if(reNewPassword.isEmpty()){
            return Output.empty_reNewPassword;
        }

        if(!reNewPassword.equals(newPassword)){
            return Output.wrong_renewPassword;
        }

        return Output.valid;
    }

    private void saveNewPassword(String currentPassword, String newPassword){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(Common.user.getEmail(), currentPassword);

        try {
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updatePassword(newPassword).addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(
                                                            getContext(),
                                                            "Thay đổi mật khẩu thành công!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(
                                                            getContext(),
                                                            "Đổi mật khẩu thất bại!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else {
                                edCurrentPass.setError("Password is wrong!");
                                edCurrentPass.requestFocus();
                            }
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(getContext(), "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}