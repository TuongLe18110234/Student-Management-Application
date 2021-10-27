package com.example.unionline.Views.Teachers.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.unionline.Adapters.Students.NotificationAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Notification;
import com.example.unionline.R;
import com.example.unionline.Sorter.NotificationDateSorter;
import com.example.unionline.Views.Teachers.TeacherAddNotificationActivity;
import com.example.unionline.Views.Teachers.TeacherListNotificationsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TeacherNotificationFragment extends Fragment {

    private FloatingActionButton fabAddNotification;
    RecyclerView recyclerView;
    ArrayList<Notification> listNotification;
    NotificationAdapter notificationAdapter;
    DatabaseReference mDatabase;
    private NotificationAdapter.RecyclerViewClickListener listener;

    public TeacherNotificationFragment() {
        // Required empty public constructor
    }

    public static TeacherNotificationFragment newInstance(String param1, String param2) {
        TeacherNotificationFragment fragment = new TeacherNotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_teacher_notification, container, false);

        setOnClickListener();
        setRecyclerView(root);

        fabAddNotification = (FloatingActionButton) root.findViewById(R.id.fbAddNotification);
        fabAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherAddNotificationActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void setOnClickListener() {
        listener = new NotificationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Notification notification = listNotification.get(position);
                openNotificationDiaLog(notification);
            }
        };
    }

    private void setRecyclerView(View root){
        recyclerView = root.findViewById(R.id.rvNotifications);

        listNotification = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(getContext(), listNotification, listener);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(notificationAdapter);

        // Get data from firebase
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
        final Dialog dialog = new Dialog(getContext());
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