package com.example.unionline.Adapters.Teachers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.unionline.Views.Teachers.Fragments.TeacherDoneApplicationFragment;
import com.example.unionline.Views.Teachers.Fragments.TeacherPendingApplicationFragment;
import com.example.unionline.Views.Teachers.TeacherAbsenceApplicationActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ApplicationPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private ArrayList<String> classIds;

    public ApplicationPagerAdapter(@NonNull FragmentManager fm, int numberOfTabs, ArrayList<String> classIds) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.classIds = classIds;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("classIds", (Serializable) classIds);

        switch (position) {
            case 0:
                TeacherPendingApplicationFragment pendingFragment = new TeacherPendingApplicationFragment();
                pendingFragment.setArguments(bundle);
                return pendingFragment;
            case 1:
                TeacherDoneApplicationFragment doneFragment = new TeacherDoneApplicationFragment();
                doneFragment.setArguments(bundle);
                return doneFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
