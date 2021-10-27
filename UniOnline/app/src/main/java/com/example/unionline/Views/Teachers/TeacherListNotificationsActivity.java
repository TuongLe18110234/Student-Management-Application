package com.example.unionline.Views.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.NotificationAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Notification;
import com.example.unionline.R;
import com.example.unionline.Sorter.NotificationDateSorter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherListNotificationsActivity extends AppCompatActivity {

    private FloatingActionButton fabAddNotification;
    RecyclerView recyclerView;
    ArrayList<Notification> listNotification;
    NotificationAdapter notificationAdapter;
    DatabaseReference mDatabase;
    private NotificationAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list_notifications);

        setToolbar();
        setOnClickListener();
        setRecyclerView();

        fabAddNotification = (FloatingActionButton) findViewById(R.id.fbAddNotification);
        fabAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherListNotificationsActivity.this, TeacherAddNotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    // Toolbar on the top of screen
    private void setToolbar() {
        // Finish activity when clicking on back item
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        // Set name for activity
        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText("Gửi thông báo");
    }

    // Open dialog to show notification detail when clicking on the notification
    private void setOnClickListener() {
        listener = new NotificationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Notification notification = listNotification.get(position);
                openNotificationDiaLog(notification);
            }
        };
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.rvNotifications);

        listNotification = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(this, listNotification, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(notificationAdapter);

        // Get data from firebase
        // Data is list notification sent by current user
        mDatabase = FirebaseDatabase.getInstance().getReference("Notifications");
        Query query = mDatabase.orderByChild("sentFrom").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNotification.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);

                    listNotification.add(notification);
                    listNotification.sort(new NotificationDateSorter());
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Create and show a message dialog when user click on notification
     * @param notification: notification to show
     */
    private void openNotificationDiaLog(Notification notification) {
        // Create and set some attributes for dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diaglog_show_notification);
        dialog.setCancelable(true);

        // Get dialog window and set some attributes for window
        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Mapping variables with view in layout
        TextView tvTittle = (TextView) dialog.findViewById(R.id.txtTittle);
        TextView tvContent = (TextView) dialog.findViewById(R.id.txtContent);
        TextView tvDate = (TextView) dialog.findViewById(R.id.txtDate);

        // Set message content corresponding to type
        tvTittle.setText(notification.getTitle());
        tvContent.setText(notification.getContent());
        tvDate.setText("Ngày gửi: " + notification.getCreateDate());

        // Show dialog
        dialog.show();
    }
}