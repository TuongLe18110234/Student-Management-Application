package com.example.unionline.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class AdminTeacherAccountActivity extends AppCompatActivity {

    private AccountAdapter teacherAdapter;
    private RecyclerView recyclerViewTeacher;
    private ArrayList<User> teacherList;
    ImageButton btnBackTeacher, btnAddNewTeacherAccount, btnSearchTeacher;
    ImageView btnEditTeacher;
    EditText editTextSearching;
    RelativeLayout teacherRootView;
    DatabaseReference adminDatabase;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_account);

        btnBackTeacher = findViewById(R.id.btn_back_quanly_giangvien);
        btnAddNewTeacherAccount = findViewById(R.id.btn_add_quanly_giangvien);
        btnSearchTeacher = findViewById(R.id.btn_admin_giangvien_search);
        btnEditTeacher = findViewById(R.id.btn_edit_giangvien);
        editTextSearching = findViewById(R.id.et_box_search_giangvien);
        teacherRootView = findViewById(R.id.giangvien_rootview);
        recyclerViewTeacher = findViewById(R.id.rv_admin_teacher_account);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users/Teacher");
        recyclerViewTeacher.setHasFixedSize(true);
        recyclerViewTeacher.setLayoutManager(new LinearLayoutManager(this));
        teacherList = new ArrayList<>();
        teacherAdapter = new AccountAdapter(this, teacherList);
        recyclerViewTeacher.setAdapter(teacherAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this::onSwipeListener);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewTeacher);

        adminDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Query query = adminDatabase.orderByChild("role").equalTo(Common.roleTeacher);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    teacherList.add(user);
                }
                teacherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBackTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminTeacherAccountActivity.super.onBackPressed();
            }
        });

        btnAddNewTeacherAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminTeacherAddFragment teacherAddFragment = new AdminTeacherAddFragment();
                teacherAddFragment.show(getSupportFragmentManager(), "Thêm Quản lý khoa");
            }
        });

        btnEditTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTeacherAccountActivity.this, AdminQlkEditActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onSwipeListener(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AccountAdapter.AccountViewHolder) {
            String nameTeacher = teacherList.get(viewHolder.getAdapterPosition()).getName();

            User teacherDelete = teacherList.get(viewHolder.getAdapterPosition());
            int indexDelete = viewHolder.getAdapterPosition();

            teacherAdapter.removeItem(indexDelete, nameTeacher);

            Snackbar snackbar = Snackbar.make(teacherRootView, nameTeacher + " removed!", Snackbar.LENGTH_LONG);
            snackbar.show();;

        }
    }
}