package com.example.unionline.Views.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.NewsAdapter;
import com.example.unionline.Adapters.Students.NotificationAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.News;
import com.example.unionline.DTO.Notification;
import com.example.unionline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentNewsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<News> newss;
    NewsAdapter newsAdapter;
    DatabaseReference mDatabase;

    TextView tvActivityName;
    ImageView backIcon;

    private NewsAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_news);

        //Set event click for back icon
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set activity name on toolbar
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText("Tin tức");

        setOnClickListener();
        setRecyclerView();

    }

    private void setOnClickListener() {
        listener = new  NewsAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(StudentNewsActivity.this, StudentNewsDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("news", (Serializable) newss.get(position));
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.rvNews);

        newss = new ArrayList<>();

        newsAdapter = new NewsAdapter(this, newss, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(newsAdapter);

        // Get list News data from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("News");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newss.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    News news = dataSnapshot.getValue(News.class);

                    // If user have this roles much see the news
                    if(news.getSentTo().contains("Sinh viên") || news.getSentTo().equals("Toàn trường")){
                        newss.add(news);
                    }
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}