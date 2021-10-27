package com.example.unionline.Views.Teachers.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.NewsAdapter;
import com.example.unionline.Adapters.Teachers.TeacherNewsAdapter;
import com.example.unionline.DTO.News;
import com.example.unionline.R;
import com.example.unionline.Views.Students.StudentNewsActivity;
import com.example.unionline.Views.Students.StudentNewsDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class TeacherHomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<News> newss;
    TeacherNewsAdapter adapter;
    DatabaseReference mDatabase;

    private TeacherNewsAdapter.RecyclerViewClickListener listener;

    Dialog dialog;
    TextView tvTittle, tvSendTo, tvSendDate, tvContent;
    ImageView imgNews;

    public TeacherHomeFragment() {
        // Required empty public constructor
    }

    public static TeacherHomeFragment newInstance(String param1, String param2) {
        TeacherHomeFragment fragment = new TeacherHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_home, container, false);

        setOnClickListener();
        setRecyclerView(root);

        return root;
    }

    // Event when user click on a news
    private void setOnClickListener() {
        listener = new  TeacherNewsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                // Get selected news
                News news = newss.get(position);
                // Open dialog to show detail of news
                openDialog(news);
            }
        };
    }

    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvNewsList);
        newss = new ArrayList<>();
        adapter = new TeacherNewsAdapter(getContext(), newss, listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        // Get data from firebase
        // Data are list news sent to teachers
        mDatabase = FirebaseDatabase.getInstance().getReference("News");
        mDatabase.orderByChild("createDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newss.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    News news = dataSnapshot.getValue(News.class);

                    // Only add news sent to teachers
                    if(news.getSentTo().contains("Giảng viên") || news.getSentTo().equals("Toàn trường")){
                        newss.add(news);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Set dialog show detail of news
    private void setDialog() {
        // Create view for dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_news, null);
        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        dialog.setContentView(view);

        // Mapping
        tvTittle = dialog.findViewById(R.id.tvTitle);
        tvContent = dialog.findViewById(R.id.tvContent);
        tvSendTo = dialog.findViewById(R.id.tvSentTo);
        tvSendDate = dialog.findViewById(R.id.tvSendDate);
        imgNews = dialog.findViewById(R.id.ivImageNews);

        // Toolbar on the top of dialog
        setToolbar();
    }

    /**
     * Show detail news dialog
     * @param news: selected dialog
     */
    private void openDialog(News news) {
        // Set values to views
        // Values from news
        tvTittle.setText(news.getTitle());
        tvContent.setText(news.getContent());
        tvSendDate.setText(news.getCreateDate());
        tvSendTo.setText(news.getSentTo());
        imgNews.setImageResource(R.drawable.new_image);

        dialog.show();
    }

    // Set top toolbar
    private void setToolbar() {
        // Dismiss the dialog when clicking on back icon
        ImageView backIcon = dialog.findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Set dialog name
        TextView txtToolbarName = dialog.findViewById(R.id.activity_name);
        txtToolbarName.setText("Tin tức");
    }
}