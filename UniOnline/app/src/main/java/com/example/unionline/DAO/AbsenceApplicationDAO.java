package com.example.unionline.DAO;

import com.example.unionline.Common;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.DTO.Enrollment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AbsenceApplicationDAO {
    String path;
    DatabaseReference mDataBase;

    private static AbsenceApplicationDAO instance;

    public static AbsenceApplicationDAO getInstance() {
        if (instance == null) {
            instance = new AbsenceApplicationDAO();
        }
        return instance;
    }

    public static void setInstance(AbsenceApplicationDAO instance) {
        AbsenceApplicationDAO.instance = instance;
    }

    public AbsenceApplicationDAO() { path = "AbsenceApplications"; }

    public void update(AbsenceApplication application) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(path)
                .child(Common.semester.getSemesterId())
                .child(String.valueOf(application.getId()))
                .setValue(application);
    }

}
