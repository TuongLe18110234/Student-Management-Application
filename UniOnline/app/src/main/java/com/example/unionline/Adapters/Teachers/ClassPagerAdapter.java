package com.example.unionline.Adapters.Teachers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.unionline.DTO.Class;
import com.example.unionline.Views.Teachers.Fragments.TeacherClassInfoFragment;
import com.example.unionline.Views.Teachers.Fragments.TeacherMarkFragment;
import com.example.unionline.Views.Teachers.Fragments.TeacherUpdateProcessFragment;

import java.io.Serializable;

public class ClassPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Class aClass;

    public ClassPagerAdapter(@NonNull FragmentManager fm, int numberOfTabs, Class aClass) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.aClass = aClass;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("class", (Serializable) aClass);

        switch (position) {
            case 0:
                TeacherClassInfoFragment teacherClassInfoFragment = new TeacherClassInfoFragment();
                teacherClassInfoFragment.setArguments(bundle);
                return teacherClassInfoFragment;
            case 1:
                TeacherUpdateProcessFragment teacherUpdateProcessFragment = new TeacherUpdateProcessFragment();
                teacherUpdateProcessFragment.setArguments(bundle);
                return teacherUpdateProcessFragment;
            case 2:
                TeacherMarkFragment teacherMarkFragment = new TeacherMarkFragment();
                teacherMarkFragment.setArguments(bundle);
                return teacherMarkFragment;
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
