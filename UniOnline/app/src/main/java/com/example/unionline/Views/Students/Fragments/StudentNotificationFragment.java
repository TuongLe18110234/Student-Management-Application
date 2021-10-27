package com.example.unionline.Views.Students.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Adapters.Students.NotificationAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Enrollment;
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

public class StudentNotificationFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Notification> listNotification;
    NotificationAdapter notificationAdapter;
    DatabaseReference mDatabase;
    
    ArrayList<String> classIds;

    private NotificationAdapter.RecyclerViewClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_student_notification, container, false);

        setOnClickListener();
        getListClasses();
        setRecyclerView(root);

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
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNotification.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);

                    for(int i=0;i<classIds.size();i++){
                        String classID = classIds.get(i);
                        if(notification.getSentTo().contains(classID)){
                            listNotification.add(notification);
                            listNotification.sort(new NotificationDateSorter());
                            break;
                        }
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getListClasses() {
        classIds = new ArrayList<String>();

        // Fill data from Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("Enrollments").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("studentId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classIds.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);

                    classIds.add(enrollment.getClassId());
                }
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
        final Dialog dialog = new Dialog(getActivity());
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