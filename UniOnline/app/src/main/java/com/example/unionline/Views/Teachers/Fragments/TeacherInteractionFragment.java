package com.example.unionline.Views.Teachers.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.unionline.R;
import com.example.unionline.Views.Teachers.TeacherAbsenceApplicationActivity;
import com.example.unionline.Views.Teachers.TeacherAddNotificationActivity;
import com.example.unionline.Views.Teachers.TeacherAttendantActivity;
import com.example.unionline.Views.Teachers.TeacherListClassesActivity;
import com.example.unionline.Views.Teachers.TeacherListNotificationsActivity;
import com.example.unionline.Views.Teachers.TeacherScheduleActivity;

public class TeacherInteractionFragment extends Fragment {

    View view;
    CardView cvSchedule, cvClass, cvAttendant, cvApplication;


    public TeacherInteractionFragment() {
        // Required empty public constructor
    }

    public static TeacherInteractionFragment newInstance(String param1, String param2) {
        TeacherInteractionFragment fragment = new TeacherInteractionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_teacher_interaction, container, false);

        mapView(view);

        cvClass.setOnClickListener(onClickListener);
        cvAttendant.setOnClickListener(onClickListener);
        cvApplication.setOnClickListener(onClickListener);
        cvSchedule.setOnClickListener(onClickListener);

        return view;
    }

    /**
     * Mapping variables with view in layout
     * @param view: fragment_teacher_interaction
     */
    private void mapView(View view) {
        cvSchedule      = (CardView) view.findViewById(R.id.cvTeacherSchedule);
        cvClass         = (CardView) view.findViewById(R.id.cvTeacherClass);
        cvAttendant     = (CardView) view.findViewById(R.id.cvTeacherAttendant);
        cvApplication   = (CardView) view.findViewById(R.id.cvTeacherApplication);
    }

    private View.OnClickListener onClickListener = (View v) -> {
        Intent intent;

        // Open activity when clicking on card view
        switch (v.getId()) {
            case R.id.cvTeacherSchedule:
                // Schedule
                intent = new Intent(this.getActivity(), TeacherScheduleActivity.class);
                startActivity(intent);
                break;
            case R.id.cvTeacherClass:
                // List classes
                intent = new Intent(this.getActivity(), TeacherListClassesActivity.class);
                startActivity(intent);
                break;
            case R.id.cvTeacherAttendant:
                // Attendant
                intent = new Intent(this.getActivity(), TeacherAttendantActivity.class);
                startActivity(intent);
                break;
            case R.id.cvTeacherApplication:
                // Absent applications
                intent = new Intent(this.getActivity(), TeacherAbsenceApplicationActivity.class);
                startActivity(intent);
                break;
        }
    };
}