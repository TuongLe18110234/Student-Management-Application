package com.example.unionline.DAO;

import com.example.unionline.DTO.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDAO {
    String path;
    DatabaseReference mDatabase;

    private static AdminDAO instance;

    public static AdminDAO getInstance() {
        if (instance==null)
            instance = new AdminDAO();
        return instance;
    }

    public static void setInstance(AdminDAO instance) { AdminDAO.instance = instance;}

    public AdminDAO() {
        path = "Users";
    }
    public void setStudentValue(User user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).child(String.valueOf(user.getUserId())).setValue(user);
    }
    public void setTeacherValue(User user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).child(String.valueOf(user.getUserId())).setValue(user);
    }
    public void setAdminValue(User user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).child(String.valueOf(user.getUserId())).setValue(user);
    }
    public void setParentValue(User user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).child(String.valueOf(user.getUserId())).setValue(user);
    }
    public void setDepartmentManagerValue(User user) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).child(String.valueOf(user.getUserId())).setValue(user);
    }

    public boolean deleteUser(String userId) {
        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(path).child(userId).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }
}
