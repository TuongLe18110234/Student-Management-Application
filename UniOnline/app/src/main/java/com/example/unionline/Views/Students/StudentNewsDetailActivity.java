package com.example.unionline.Views.Students;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.News;
import com.example.unionline.R;

public class StudentNewsDetailActivity extends AppCompatActivity {

    ImageView ivImageNews, backIcon;
    TextView tvSentTo, tvSendDate, tvTitle, tvContent, tvActivityName;

    News currentNews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_news_detail);

        // Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentNews = (News) bundle.getSerializable("news");
        }

        //Set event click for back icon
        backIcon = (ImageView) findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set activity name on toolbar
        tvActivityName = (TextView) findViewById(R.id.activity_name);
        tvActivityName.setText("Tin tức");

        ivImageNews = findViewById(R.id.ivImageNews);
        ivImageNews.setImageResource(R.drawable.new_image);

        tvSentTo = findViewById(R.id.tvSentTo);
        tvSentTo.setText(currentNews.getSentTo());

        tvSendDate = findViewById(R.id.tvSendDate);
        tvSendDate.setText(currentNews.getCreateDate());

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(currentNews.getTitle());

        tvContent = findViewById(R.id.tvContent);
        tvContent.setText(currentNews.getContent());
    }
}