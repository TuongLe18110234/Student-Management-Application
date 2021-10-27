package com.example.unionline.Views.Teachers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.R;
import com.example.unionline.Views.Teachers.Fragments.TeacherChooseClassFragment;

public class TeacherAddNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_notification);

        // Replace with fragment choose class to send notification
        getSupportFragmentManager().beginTransaction().replace(R.id.main, new TeacherChooseClassFragment())
                .addToBackStack(null)
                .commit();
    }
}