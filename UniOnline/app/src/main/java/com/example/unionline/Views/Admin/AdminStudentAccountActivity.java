package com.example.unionline.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.unionline.Adapters.Admin.AccountAdapter;
import com.example.unionline.Adapters.Admin.RecyclerViewItemTouchHelper;
import com.example.unionline.Common;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdminStudentAccountActivity extends AppCompatActivity {

    private AccountAdapter accountAdapter;
    private RecyclerView recyclerViewStudent;
    private ArrayList<User> userList;
    ImageButton btnBack, btnAddNewStudentAccount, btnSearch;
    ImageView btnEditStudent;
    EditText editTextSearching;
    Intent intent;
    RelativeLayout StudentRootView;
    DatabaseReference adminDatabase;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_account);

        btnBack = findViewById(R.id.btn_back_quanly_sinhvien);
        btnAddNewStudentAccount = findViewById(R.id.btn_add_quanly_sinhvien);
        btnSearch = findViewById(R.id.btn_admin_student_search);
        btnEditStudent = findViewById(R.id.btn_edit_student);
        editTextSearching = findViewById(R.id.et_box_search_student);
        recyclerViewStudent = findViewById(R.id.rv_admin_student_account);
        linearLayoutManager = new LinearLayoutManager(this);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users/Student");
        recyclerViewStudent.setHasFixedSize(true);
        recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, userList);
        recyclerViewStudent.setAdapter(accountAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this::onSwipeListener);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewStudent);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Query query = adminDatabase.orderByChild("role").equalTo(Common.roleStudent);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                accountAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminStudentAccountActivity.super.onBackPressed();
            }
        });

        btnAddNewStudentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminStudentAddFragment adminStudentAddFragment = new AdminStudentAddFragment();
                adminStudentAddFragment.show(getSupportFragmentManager(), "Thêm Sinh viên");
            }
        });

        btnEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminStudentEditFragment adminStudentEditFragment = new AdminStudentEditFragment();
                adminStudentEditFragment.show(getSupportFragmentManager(), "Chỉnh sửa Sinh viên");
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editTextSearching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String text){
        userList = new ArrayList<>();
        ArrayList<User> mExamList = new ArrayList<>();

        for (User user : mExamList){
            if(user.getName().toLowerCase().contains(text.toLowerCase())){
                userList.add(user);
            }
        }
        AccountAdapter.filterList(userList);
    }

    public void onSwipeListener(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AccountAdapter.AccountViewHolder) {
            String nameStudent = userList.get(viewHolder.getAdapterPosition()).getName();

            User StudentDelete = userList.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            accountAdapter.removeItem(indexDelete, nameStudent);

            Snackbar snackbar = Snackbar.make(StudentRootView, nameStudent + " removed!", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountAdapter.undoItem(StudentDelete, indexDelete);
                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }
}