package com.example.unionline.Views.Students.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unionline.R;
import com.example.unionline.Views.Students.StudentAbsenceApplicationActivity;
import com.example.unionline.Views.Students.StudentEnrollmentActivity;
import com.example.unionline.Views.Students.StudentMainActivity;
import com.example.unionline.Views.Students.StudentMarkActivity;
import com.example.unionline.Views.Students.StudentNewsActivity;
import com.example.unionline.Views.Students.StudentScheduleActivity;
import com.example.unionline.Views.Students.StudentTuitionActivity;

import java.util.Calendar;


public class StudentInteractionFragment extends Fragment {

    CardView cvSchedule, cvClass, cvMark, cvStudentApplication,
            cvNews, cvStudentTuition, cvStudentNotification, cvStudentAccount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student_interaction, container, false);

        mapView(view);

        return view;
    }

    /**
     * Map view form fragment with variable
     * @param view
     */
    private void mapView(View view){
        // CardView open Schedule
        cvSchedule = view.findViewById(R.id.cvStudentSchedule);
        cvSchedule.setOnClickListener(onClickListener);

        // CardView open Class
        cvClass = view.findViewById(R.id.cvStudentClass);
        cvClass.setOnClickListener(onClickListener);

        // CardView open Student Mark
        cvMark = view.findViewById(R.id.cvStudentMark);
        cvMark.setOnClickListener(onClickListener);

        // CardView open student application
        cvStudentApplication = view.findViewById(R.id.cvStudentApplication);
        cvStudentApplication.setOnClickListener(onClickListener);

        // CardView open news from manager
        cvNews = view.findViewById(R.id.cvStudentNews);
        cvNews.setOnClickListener(onClickListener);

        // CardView open Tuition
        cvStudentTuition = view.findViewById(R.id.cvStudentTuition);
        cvStudentTuition.setOnClickListener(onClickListener);

        // CardView open Notification for student
        cvStudentNotification = view.findViewById(R.id.cvStudentNotification);
        cvStudentNotification.setOnClickListener(onClickListener);

        // // CardView open account
        cvStudentAccount = view.findViewById(R.id.cvStudentAccount);
        cvStudentAccount.setOnClickListener(onClickListener);
    }

    /**
     * Open activity with CardView click
     */
    private View.OnClickListener onClickListener = (View v) -> {
        Intent intent;
        switch (v.getId()) {
            case R.id.cvStudentSchedule:
                intent = new Intent(this.getActivity(), StudentScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.cvStudentClass:
                intent = new Intent(this.getActivity(), StudentEnrollmentActivity.class);
                startActivity(intent);
                break;
            case R.id.cvStudentMark:
                intent = new Intent(this.getActivity(), StudentMarkActivity.class);
                startActivity(intent);
                break;
            case R.id.cvStudentApplication:
                intent = new Intent(this.getActivity(), StudentAbsenceApplicationActivity.class);
                startActivity(intent);
                break;
            case R.id.cvStudentNews:
                intent = new Intent(this.getActivity(), StudentNewsActivity.class);
                startActivity(intent);
                break;
            case R.id.cvStudentTuition:
                intent = new Intent(this.getActivity(), StudentTuitionActivity.class);
                startActivity(intent);
                break;
            case R.id.cvStudentNotification:
                // Select navigation notification
                ((StudentMainActivity)getActivity()).bottomNav.setSelectedItemId(R.id.nav_student_notification);
                break;
            case R.id.cvStudentAccount:
                // Select navigation account
                ((StudentMainActivity)getActivity()).bottomNav.setSelectedItemId(R.id.nav_student_account);
                break;
        }
    };
}