package com.example.unionline.DAO;

import com.example.unionline.DTO.Lesson;
import com.example.unionline.DTO.Notification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationDAO {
    String path;
    DatabaseReference mDataBase;

    private static NotificationDAO instance;

    public static NotificationDAO getInstance() {
        if (instance == null) {
            instance = new NotificationDAO();
        }
        return instance;
    }

    public static void setInstance(NotificationDAO instance) {
        NotificationDAO.instance = instance;
    }

    public NotificationDAO() { path = "Notifications"; }

    public void insertNotification(Notification notification) {
        mDataBase = FirebaseDatabase.getInstance().getReference(path);
        String key = mDataBase.push().getKey();
        notification.setId(key);
        mDataBase.child(key).setValue(notification);
    }
    public void setValude(Notification notification)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Notifications").child(notification.getId()).setValue(notification);
    }
    public boolean deleteNotification(String id) {
        try {
            mDataBase = FirebaseDatabase.getInstance().getReference();
            mDataBase.child(path).child(id).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }
}
