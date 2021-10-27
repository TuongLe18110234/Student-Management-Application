package com.example.unionline.DAO;

import com.example.unionline.Common;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.DTO.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDAO {
    String path;
    DatabaseReference mDataBase;

    private static UserDAO instance;

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public static void setInstance(UserDAO instance) {
        UserDAO.instance = instance;
    }

    public UserDAO() { path = "Users"; }

    public void update(User user) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(path)
                .child(user.getUserId())
                .setValue(user);
    }
}
