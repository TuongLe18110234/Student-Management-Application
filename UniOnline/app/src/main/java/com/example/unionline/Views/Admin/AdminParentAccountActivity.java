package com.example.unionline.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class AdminParentAccountActivity extends AppCompatActivity {

    private AccountAdapter accountAdapter;
    private RecyclerView recyclerViewParent;
    private ArrayList<User> userList;
    ImageButton btnBack, btnAddNewParentAccount, btnSearch;
    ImageView btnEditParent;
    EditText editTextSearching;
    Intent intent;
    RelativeLayout parentRootView;
    DatabaseReference adminDatabase;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_parent_account);

        btnBack = findViewById(R.id.ib_back_quanly_phuhuynh);
        btnAddNewParentAccount = findViewById(R.id.btn_add_quanly_phuhuynh);
        btnSearch = findViewById(R.id.btn_admin_parent_search);
        btnEditParent = findViewById(R.id.btn_edit_parent);
        editTextSearching = findViewById(R.id.et_box_search_parent);
        recyclerViewParent = findViewById(R.id.rv_admin_parent_account);
        linearLayoutManager = new LinearLayoutManager(this);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users/Parent");
        recyclerViewParent.setHasFixedSize(true);
        recyclerViewParent.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        accountAdapter = new AccountAdapter(this, userList);
        recyclerViewParent.setAdapter(accountAdapter);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Query query = adminDatabase.orderByChild("role").equalTo(Common.roleParent);
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
                AdminParentAccountActivity.super.onBackPressed();
            }
        });

        btnAddNewParentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminParentAddFragment adminParentAddFragment = new AdminParentAddFragment();
                adminParentAddFragment.show(getSupportFragmentManager(), "Thêm Phụ huynh");
            }
        });

        btnEditParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminParentAccountActivity.this, AdminParentEditActivity.class);
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

    public void onSwipeListener(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AccountAdapter.AccountViewHolder) {
            String nameParent = userList.get(viewHolder.getAdapterPosition()).getName();

            User ParentDelete = userList.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            accountAdapter.removeItem(indexDelete, nameParent);

            Snackbar snackbar = Snackbar.make(parentRootView, nameParent + " removed!", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountAdapter.undoItem(ParentDelete, indexDelete);
                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }
}