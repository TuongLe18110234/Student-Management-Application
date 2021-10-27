package com.example.unionline.Views.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unionline.Adapters.Students.LessonAdapter;
import com.example.unionline.Adapters.Teachers.ScheduleAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TeacherScheduleActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Lesson> allLessons;
    ArrayList<Lesson> filterLessons;
    ArrayList<Class> classes;
    ArrayList<String> classIds;
    ScheduleAdapter adapter;
    DatabaseReference mDatabase;

    TextView tvCurrentDate, tvChooseOtherDate, tvMonth;
    CompactCalendarView calendarView;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        // Open calendar when clicking on "Chọn ngày khác"
        tvCurrentDate = findViewById(R.id.txtDate);
        tvChooseOtherDate = findViewById(R.id.txtOtherDate);
        tvChooseOtherDate.setOnClickListener((View v) -> {
            try {
                openCalendarDialog();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        // Set current date
        Date createDate = new Date();
        tvCurrentDate.setText(formatter.format(createDate));

        setToolbar();
        setRecyclerView();
    }

    private void setRecyclerView() {
        // Initialize
        recyclerView = findViewById(R.id.rvListLessons);

        // Some list to save data
        filterLessons  = new ArrayList<>();
        allLessons = new ArrayList<>();
        classes = new ArrayList<>();
        classIds = new ArrayList<>();

        adapter = new ScheduleAdapter(this, filterLessons, classes);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set adapter for recycler view
        recyclerView.setAdapter(adapter);

        // Fill data from Firebase
        // Data is list classes that current teacher is teaching
        mDatabase = FirebaseDatabase.getInstance().getReference("Classes").child(Common.semester.getSemesterId());
        Query query = mDatabase.orderByChild("teacherId").equalTo(Common.user.getUserId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classes.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Class aClass = dataSnapshot.getValue(Class.class);
                    classes.add(aClass);
                    classIds.add(aClass.getClassId());
                }
                adapter.notifyDataSetChanged();
                Log.e("#class", String.valueOf(classIds.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Fill data from Firebase
        // Data is list lessons in classes above
        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons").child(Common.semester.getSemesterId());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allLessons.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Lesson lesson = dataSnapshot.getValue(Lesson.class);
                    if(classIds.contains(lesson.getClassId())) {
                        allLessons.add(lesson);
                    }
                }

                // Only get lessons in selected date
                filtLessons();
                Log.e("#lesson", String.valueOf(allLessons.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openCalendarDialog() throws ParseException {
        // Create and set some attributes for dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_calendar);
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

        calendarView = (CompactCalendarView) dialog.findViewById(R.id.compactcalendar_view);
        calendarView.setUseThreeLetterAbbreviation(true);

        // Month on the top of calendar
        tvMonth = dialog.findViewById(R.id.txtMonth);
        tvMonth.setText(dateFormatMonth.format(new Date()));

        for(Lesson lesson : allLessons) {
            // For each lessons, add an event on calendar
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(lesson.getDate());
            int greenColor = this.getColor(R.color.successColor);
            Event event = new Event(greenColor, date.getTime(), lesson.getName());
            calendarView.addEvent(event);
        }

        // When selecting a date --> set current date is selected date
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                tvCurrentDate.setText(formatter.format(dateClicked));
                filtLessons();
                dialog.dismiss();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tvMonth.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        dialog.show();
    }

    // Only get lesson of current date
    private void filtLessons() {
        String date = tvCurrentDate.getText().toString();

        filterLessons.clear();
        for (Lesson l : allLessons) {
            if(date.equals(l.getDate())) {
                filterLessons.add(l);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setToolbar() {
        ImageView backIcon = findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            this.finish();
        });

        TextView txtToolbarName = findViewById(R.id.activity_name);
        txtToolbarName.setText("Thời khóa biểu");
    }
}