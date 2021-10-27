package com.example.unionline.DAO;

import androidx.annotation.NonNull;

import com.example.unionline.Common;
import com.example.unionline.DTO.Class;
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

public class LessonDAO {
    String path;
    DatabaseReference mDataBase;
    boolean error;
    private static LessonDAO instance;

    public static LessonDAO getInstance() {
        if (instance == null) {
            instance = new LessonDAO();
        }
        return instance;
    }

    public static void setInstance(LessonDAO instance) {
        LessonDAO.instance = instance;
    }

    public LessonDAO() { path = "Lessons"; }


    public void changeStatusLesson(Lesson lesson, String semesterId) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(path)
                .child(semesterId)
                .child(String.valueOf(lesson.getLessonId()))
                .child("status").setValue(!lesson.isStatus());
    }

    public void DeleteAllLessonByClassModel(ClassModel1 classModel1)
    {
        boolean status=false;
        List<Lesson> lessons=new ArrayList<>();
        System.out.println("Class Id"+classModel1.getClassId());
        mDataBase = FirebaseDatabase.getInstance().getReference("Lessons").child(classModel1.getSemesterId());
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessons.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Lesson lesson= dataSnapshot.getValue(Lesson.class);
                    if(lesson.getClassId().equals(classModel1.getClassId()))
                            deleteLesson(lesson,classModel1);

                }

                System.out.println("List lesson"+lessons.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void update(Lesson lesson) {
        mDataBase = FirebaseDatabase.getInstance().getReference(path);
        mDataBase.child(Common.semester.getSemesterId())
                .child(lesson.getLessonId())
                .setValue(lesson);
    }
    public void deleteLesson(Lesson lesson,ClassModel1 classModel1) {
        try {
            mDataBase = FirebaseDatabase.getInstance().getReference();
            mDataBase.child(path).child(classModel1.getSemesterId()).child(lesson.getLessonId()).removeValue();

        } catch (Error error){

        }
    }

    public void setValude(Lesson lesson,ClassModel1 classModel1)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Lessons").child(classModel1.getSemesterId()).child(lesson.getLessonId()).setValue(lesson);
    }
    public boolean UpdateLessonByClassModel(ClassModel1 classModelOld,ClassModel1 classModelNew)
    {
        error=true;
        List<Lesson> lessons=new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference("Lessons").child(classModelOld.getSemesterId());
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessons.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    if (lesson.getClassId().equals(classModelOld.getClassId())) {
                                    lessons.add(lesson);

                    }
                }
                Date date=convertStringToDate(classModelNew.getStartDate());
                Calendar calendar=dateToCalendar(date);
                calendar.setTime(date);
                for(int i=0;i<lessons.size();i++)
                {
                    if(lessons.get(i).getWeek()==1)
                    {
                        Date dateAdd=calendar.getTime();
                        String dateAddString=convertDateToString(dateAdd);
                        lessons.get(i).setDate(dateAddString);
                        lessons.get(i).setClassId(classModelNew.getClassId());

                        setValude(lessons.get(i),classModelNew);
                    }
                    else {
                        calendar=dateToCalendar(date);
                        calendar.add(Calendar.DATE,7*lessons.get(i).getWeek());
                        Date dateAdd = calendar.getTime();
                        String dateAddString = convertDateToString(dateAdd);
                        lessons.get(i).setDate(dateAddString);
                        lessons.get(i).setClassId(classModelNew.getClassId());

                        setValude(lessons.get(i),classModelNew);

                    }

                }
                error=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return error;
    }
    public void GetAllLessonByClass(List<Lesson> lessons,ClassModel1 classModel1)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference("Lessons").child(classModel1.getSemesterId());
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessons.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Lesson lesson= dataSnapshot.getValue(Lesson.class);
                    if(lesson.getClassId().equals(classModel1.getClassId()))
                        lessons.add(lesson);

                }

                System.out.println("List lesson"+lessons.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    public List<Lesson> getAllLessonByClass(Class classObject)
    {
        List<Lesson> lessons=new ArrayList<>();
        mDataBase = FirebaseDatabase.getInstance().getReference("Lessons").child(classObject.getSemesterId());
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessons.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Lesson lesson= dataSnapshot.getValue(Lesson.class);
                    if(lesson.getClassId().equals(classObject.getClassId()))
                        lessons.add(lesson);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return lessons;
    }
}
