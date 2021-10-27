package com.example.unionline.DAO;

import androidx.annotation.NonNull;

import com.example.unionline.Common;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.ClassModel1;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.Lesson;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendanceDAO {
    String path;
    DatabaseReference mDataBase;

    private static AttendanceDAO instance;
    Boolean error;

    public static AttendanceDAO getInstance() {
        if (instance == null) {
            instance = new AttendanceDAO();
        }
        return instance;
    }

    public static void setInstance(AttendanceDAO instance) {
        AttendanceDAO.instance = instance;
    }

    public AttendanceDAO() { path = "Attendances"; }

    public void changeAttendanceState(Attendance attendance) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(path)
                .child(Common.semester.getSemesterId())
                .child(String.valueOf(attendance.getId()))
                .child("state").setValue(attendance.getState());
    }
    public boolean deleteAntendence(Attendance attendance, ClassModel1 classModel1) {
        try {
            mDataBase = FirebaseDatabase.getInstance().getReference();
            mDataBase.child(path).child(classModel1.getSemesterId()).child(attendance.getId()).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }
    public void DeleteAllAllAtendenceByClassModel(ClassModel1 classModel1)
    {
        System.out.println("Class Id"+classModel1.getClassId());
        mDataBase = FirebaseDatabase.getInstance().getReference("Attendances").child(classModel1.getSemesterId());
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Attendance attendance= dataSnapshot.getValue(Attendance.class);
                    if(attendance.getClassId().equals(classModel1.getClassId()))
                        deleteAntendence(attendance,classModel1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean UpdateAttendenceByClassModel(ClassModel1 classModelOld,ClassModel1 classModelNew)
    {
        error=true;
        List<Attendance> attendances=new ArrayList<>();
        List<Lesson> lessonList=new ArrayList<>();
        LessonDAO.getInstance().GetAllLessonByClass(lessonList,classModelNew);
        mDataBase = FirebaseDatabase.getInstance().getReference("Attendances").child(classModelOld.getSemesterId());
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date date = convertStringToDate(classModelNew.getStartDate());
                Calendar calendar = dateToCalendar(date);
                calendar.setTime(date);
                System.out.println("value of classID"+classModelOld.getClassId());
                attendances.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);
                    if (attendance.getClassId().equals(classModelOld.getClassId())) {
                        attendances.add(attendance);
                    }
                }
                System.out.println("List of lesson "+String.valueOf(lessonList.size()));
                System.out.println("List attendances "+attendances.size());
                for(int index=0;index<lessonList.size();index++) {
                    for (int i = 0; i < attendances.size(); i++) {
                        if(lessonList.get(index).getLessonId().equals(attendances.get(i).getLessonId())) {
                            System.out.println(attendances.get(i).toString());
                            attendances.get(i).setClassId(classModelNew.getClassId());
                            attendances.get(i).setClassName(classModelNew.getClassName());
                            attendances.get(i).setClassRoom(classModelNew.getRoom());
                            attendances.get(i).setFullDate(lessonList.get(index).getDate());
                            attendances.get(i).setLessonName(lessonList.get(index).getName());
                            //attendance.setFullDate(lesson.getDate());
                            setValude(attendances.get(i), classModelNew);
                        }
                        //deleteAntendence(attendances.get(i),classModelOld);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return error;
    }
    public void setValude(Attendance attendance,ClassModel1 classModel1)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Attendances").child(classModel1.getSemesterId()).child(attendance.getId()).setValue(attendance);
    }
    public Date convertStringToDate(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
            date = null;
        }
        return date;
    }

    public Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }
    public String convertDateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }
}
//                System.out.println("List lesson in attendences"+lessonList.size());
//                int i =0;
//                for(Lesson lesson: lessonList)
//                    {
//                        for(Attendance attendance:attendances)
//                        {
//                            if(lesson.getLessonId().equals(attendance.getLessonId()))
//                            {
//                                attendance.setClassId(classModelNew.getClassId());
//                                attendance.setClassName(classModelNew.getClassName());
//                                attendance.setClassRoom(classModelNew.getRoom());
//                                attendance.setFullDate(lesson.getDate());
//                                setValude(attendance,classModelNew);
//                            }
//                            i++;
//                        }
//                    }
//System.out.println("Vong lap la "+String.valueOf(i));

//                    int week=Integer.parseInt(splits[1]);
//                    if(attendance.getClassId().equals(classModelOld.getClassId()))
//                    {
//                        if(week==1)
//                        {
//                            Date dateAdd=calendar.getTime();
//                            String dateAddString=convertDateToString(dateAdd);
//                            attendance.setFullDate(dateAddString);
//                        }
//                        else {
//                            calendar.roll(Calendar.DATE,7*week);
//                            Date dateAdd=calendar.getTime();
//                            String dateAddString=convertDateToString(dateAdd);
//                            attendance.setFullDate(dateAddString);
//
//                        }
//                for(int i=0;i<attendances.size();i++)
//                        {
//                            if(classModelOld.getClassId().equals(attendances.get(i).getClassId()))
//                            {
//
//                                attendances.get(i).setClassId(classModelNew.getClassId());
//                                attendances.get(i).setClassName(classModelNew.getClassName());
//                                attendances.get(i).setClassRoom(classModelNew.getRoom());
//                                    //attendance.setFullDate(lesson.getDate());
//                            }
//                            setValude(attendances.get(i), classModelNew);
//                        }

