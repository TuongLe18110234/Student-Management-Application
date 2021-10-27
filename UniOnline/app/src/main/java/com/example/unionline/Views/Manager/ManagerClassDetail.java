package com.example.unionline.Views.Manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.unionline.Adapters.Managers.EnrollmentApdapter;
import com.example.unionline.Adapters.Managers.StudentAdapter;
import com.example.unionline.Adapters.Managers.LessonAdapter;
import com.example.unionline.Adapters.Students.PageAdapter;
import com.example.unionline.DAO.AttendanceDAO;
import com.example.unionline.DAO.EnrollmentDAO;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.ClassModel1;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManagerClassDetail extends Fragment {

    public PageAdapter pageAdapter;
    ImageView backIcon;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabStudentClass, tabLesson;
    private ClassModel1 classModel1;
    DatabaseReference mDatabase;
    ArrayList<Enrollment> enrollments;
    ArrayList<Lesson> lessons;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    LessonAdapter lessonAdapter;
    EnrollmentApdapter enrollmentApdapter;
    private int StateOfRecycleview;
    private LessonAdapter.RecyclerViewClickListener listener;
    private EnrollmentApdapter.RecyclerViewClickListener listenerEnrollment;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manager_class_detail, container, false);
        Bundle bundleRecive=getArguments();
        ClassModel1 classModel1=(ClassModel1) bundleRecive.get("classChose");
        //loadStudentEnrollClass(enrollments,classModel1);
        List<Attendance> attendances=new ArrayList<>();
        loadStudentAttendences(attendances,classModel1);
        StateOfRecycleview=0;

        setRecyclerView(root,classModel1);

        backIcon =  root.findViewById(R.id.left_icon);
        backIcon.setOnClickListener((View v) -> {
            if(getFragmentManager() !=null)
            {
                getFragmentManager().popBackStack();
            }
        });
        tabLayout = root.findViewById(R.id.manager_tabClassDetail);
        tabStudentClass = root.findViewById(R.id.tab_studentAttendenceClass);
        tabLesson = root.findViewById(R.id.tab_managerLeson);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    StateOfRecycleview=0;
                    setRecyclerView(root,classModel1);
                } else if (tab.getPosition() == 1) {
                    StateOfRecycleview=1;
                    setRecyclerViewForTabLesson(root,classModel1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String message="";
                if(StateOfRecycleview==0) {
//                    Enrollment enrollmentFind=enrollments.get(viewHolder.getAdapterPosition());
//                    boolean status = EnrollmentDAO.getInstance().deleteEnrollMent(enrollmentFind,classModel1);;
//                    if (status) {
//                        boolean statusDeleteAttendence=deleteAtendencesStudent(attendances,enrollmentFind,classModel1);
//                        if(statusDeleteAttendence)
//                            Toast.makeText(getContext(), "Delete student of class completed", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(getContext(), "Delete Attendences Failed", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "Delete Student of class failed", Toast.LENGTH_SHORT).show();
//                    }
                    message="Do yo want delete student in this class ";
                }
                else {
                    message = "Nothing there!!!";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setMessage(message)
                        // positiveButton là nút thuận : đặt là OK
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(StateOfRecycleview==0) {
                                    Enrollment enrollmentFind=enrollments.get(viewHolder.getAdapterPosition());
                                    boolean status = EnrollmentDAO.getInstance().deleteEnrollMent(enrollmentFind,classModel1);;
                                    if (status) {
                                        boolean statusDeleteAttendence=deleteAtendencesStudent(attendances,enrollmentFind,classModel1);
                                        if(statusDeleteAttendence)
                                            Toast.makeText(getContext(), "Delete student of class completed", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(getContext(), "Delete Attendences Failed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Delete Student of class failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        // ngược lại negative là nút nghịch : đặt là cancel
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(StateOfRecycleview==0)
                                    enrollmentApdapter.notifyDataSetChanged();
                                else
                                    lessonAdapter.notifyDataSetChanged();
                                // User cancelled the dialog
                                //notificationAdapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
                if(StateOfRecycleview==0)
                    enrollmentApdapter.notifyDataSetChanged();
                else
                    lessonAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
        return  root;

    }

    // Set recrycleView for Student into class
    private void setRecyclerView(View root,ClassModel1 classModel1)
    {
        recyclerView=root.findViewById(R.id.recyleViewDetail);
        enrollments=new ArrayList<>();

        enrollmentApdapter=new EnrollmentApdapter(getContext(),enrollments,listenerEnrollment);
        //System.out.println(classUsers.size());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1, RecyclerView.VERTICAL,false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(enrollmentApdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Enrollments").child(classModel1.getSemesterId());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrollments.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    if (enrollment.getClassId().equals(classModel1.getClassId())) {
                        enrollments.add(enrollment);
                    }


                }
                enrollmentApdapter.notifyDataSetChanged();
                System.out.println(enrollments.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loadStudentEnrollClass(List<Enrollment> enrollments,ClassModel1 classModel1)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Enrollments").child(classModel1.getSemesterId());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                enrollments.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Enrollment enrollment = dataSnapshot.getValue(Enrollment.class);
                    if (enrollment.getClassId().equals(classModel1.getClassId())) {
                        enrollments.add(enrollment);
                        //System.out.println("Class lesson Id" + lesson.getClassId());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void loadStudentAttendences(List<Attendance> attendances,ClassModel1 classModel1)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Attendances").child(classModel1.getSemesterId());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendances.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Attendance attendance = dataSnapshot.getValue(Attendance.class);
                    if (attendance.getClassId().equals(classModel1.getClassId())) {
                        attendances.add(attendance);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setRecyclerViewForTabLesson(View root,ClassModel1 classModel1)
    {
        recyclerView=root.findViewById(R.id.recyleViewDetail);
        lessons=new ArrayList<>();

        lessonAdapter=new LessonAdapter(getContext(),lessons,listener);
        System.out.println(lessons.size());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1, RecyclerView.VERTICAL,false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(lessonAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("Lessons").child(classModel1.getSemesterId());
        mDatabase.addValueEventListener(new ValueEventListener() {
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
                lessonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public Enrollment findEnrollmentWithUserAndClass(List<Enrollment> enrollments,User user,ClassModel1 classModel1)
    {
        Enrollment enrollmentFind=new Enrollment();
        for(Enrollment enrollment:enrollments)
        {
            if((enrollment.getStudentId().equals(user.getUserId()))&&(enrollment.getClassId().equals(classModel1.getClassId())))
                enrollmentFind=enrollment;
        }
        return enrollmentFind;
    }
    public boolean deleteAtendencesStudent(List<Attendance> attendances,Enrollment enrollment,ClassModel1 classModel1)
    {
        boolean status=false;
        for(Attendance attendance: attendances)
        {
            if((attendance.getClassId().equals(enrollment.getClassId()))&&(attendance.getStudentId().equals(enrollment.getStudentId())))
                status= AttendanceDAO.getInstance().deleteAntendence(attendance,classModel1);
        }
        return status;
    }


    // Find enrollment with userID and ClassID
    public Enrollment findEnrollmentByIdUserAndClass(List<Enrollment> enrollments,User user,ClassModel1 classModel1)
    {
        Enrollment enrollmentFind=new Enrollment();
        for(Enrollment enrollment: enrollments)
        {
            if((enrollment.getClassId().equals(classModel1.getClassId()))&&(enrollment.getStudentId().equals(user.getUserId())))
                enrollmentFind=enrollment;
        }
        return enrollmentFind;
    }
}
