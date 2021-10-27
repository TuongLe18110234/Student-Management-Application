package com.example.unionline.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.unionline.Adapters.Admin.AccountAdapter;
import com.example.unionline.Adapters.Admin.RecyclerViewItemTouchHelper;
import com.example.unionline.Common;
import com.example.unionline.DAO.AdminDAO;
import com.example.unionline.DTO.Lesson;
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

public class AdminQlkAccountActivity extends AppCompatActivity {

    private AccountAdapter accountAdapter;
    private RecyclerView recyclerViewQlk;
    private ArrayList<User> userList;
    ImageButton btnBack, btnAddNewQlkAccount, btnSearch;
    ImageView btnEditQlk;
    EditText editTextSearching;
    RelativeLayout qlkRootView;
    DatabaseReference adminDatabase;
    LinearLayoutManager linearLayoutManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_qlk_account);

        btnBack = findViewById(R.id.btn_back_quanly);

        btnEditQlk = findViewById(R.id.btn_edit_qlk);
        editTextSearching = findViewById(R.id.et_box_search_qlk);
        recyclerViewQlk = findViewById(R.id.rv_admin_qlkhoa_account);
        linearLayoutManager = new LinearLayoutManager(this);
        qlkRootView = findViewById(R.id.qlk_rootview);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users/Department Manager");
        recyclerViewQlk.setHasFixedSize(true);
        recyclerViewQlk.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, userList);
        recyclerViewQlk.setAdapter(accountAdapter);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Query query = adminDatabase.orderByChild("role").equalTo(Common.roleManager);
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminQlkAccountActivity.super.onBackPressed();
            }
        });

        btnAddNewQlkAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminQlkAddFragment adminQlkAddFragment = new AdminQlkAddFragment();
                adminQlkAddFragment.show(getSupportFragmentManager(), "Thêm Quản lý khoa");
            }
        });

        btnEditQlk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminQlkAccountActivity.this, AdminQlkEditActivity.class);
                startActivity(intent);
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

}