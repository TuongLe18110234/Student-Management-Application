package com.example.unionline.Views.Manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.implementproject.R;
//import com.example.implementproject.adapter.StudentAdapter;
//import com.example.implementproject.model.Attendance;
//import com.example.implementproject.model.ClassModel1;
//import com.example.implementproject.model.Enrollment;
//import com.example.implementproject.model.Lesson;
//import com.example.implementproject.model.User;
import com.example.unionline.Adapters.Managers.StudentAdapter;
import com.example.unionline.Common;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.DTO.ClassModel1;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.DTO.User;
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
import com.example.unionline.R;

public class ManageStudentControllers extends Fragment implements View.OnClickListener
{

    public static final String TAG=ManageStudentControllers.class.getName();
    RecyclerView recyclerView;
    ArrayList<User> classUsers;
    StudentAdapter studentAdapter;
    TextView tvClassID,tvClassName,tvTeacher,tvCapicity,tvRoom;
    Button btClose;
    ImageButton imageButton,imageButtonAddToClass;
    DatabaseReference mDatabase;
    private StudentAdapter.RecyclerViewClickListener listener;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root=inflater.inflate(R.layout.fragment_manager_studenttoclass,container,false);
        Bundle bundleRecive=getArguments();
        ClassModel1 classModel1=(ClassModel1) bundleRecive.get("classChose");
        setOnClickListener();
        List<Enrollment> enrollments=new ArrayList<>();
        loadStudentEnrollClass(enrollments,classModel1);
        setRecyclerView(root,enrollments);


        tvClassID=root.findViewById(R.id.tvHocPhan);
        tvClassName=root.findViewById(R.id.tvLop);
        tvTeacher=root.findViewById(R.id.tvGVien);
        tvCapicity=root.findViewById(R.id.tvSSo);
        tvRoom=root.findViewById(R.id.tvPhong);
        tvClassID.setText(classModel1.getClassId());
        tvClassName.setText(classModel1.getClassName());
        tvTeacher.setText(classModel1.getTeacherId());
        tvCapicity.setText(String.valueOf(classModel1.getCapacity()));
        tvRoom.setText(classModel1.getRoom());
        imageButton=root.findViewById(R.id.imageButton16);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFragmentManager() !=null)
                {
                    getFragmentManager().popBackStack();
                }
            }
        });
        imageButtonAddToClass=root.findViewById(R.id.imageButtonAddCToClass);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                // set Message là phương thức thiết lập câu thông báo
                builder.setMessage("Do you want add student to class!!!")
                        // positiveButton là nút thuận : đặt là OK
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                User user=classUsers.get(viewHolder.getAdapterPosition());
                                AddStudentToClass(classModel1,user);
                                classUsers.remove(viewHolder.getAdapterPosition());
                                studentAdapter.notifyDataSetChanged();
                            }
                        })
                        // ngược lại negative là nút nghịch : đặt là cancel
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                studentAdapter.notifyDataSetChanged();
                            }
                        });
                // tạo dialog và hiển thị
                builder.create().show();
                studentAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
        return root;
    }


    //Set recyclerView of enrollment class. Have show userID student and name student.
    private void setRecyclerView(View root,List<Enrollment> enrollments)
    {
        recyclerView=root.findViewById(R.id.rycyclerview_themsinhvien);
        classUsers=new ArrayList<>();

        studentAdapter=new StudentAdapter(getContext(),classUsers,listener);
        System.out.println(classUsers.size());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(studentAdapter);
        List<User> userListToRemove=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classUsers.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    User user= dataSnapshot.getValue(User.class);
                    if(user.getRole()==3)
                                classUsers.add(user);

                }
                for(User user:classUsers)
                {
                    for(Enrollment enrollment: enrollments)
                    {
                            if(enrollment.getStudentId().equals(user.getUserId()))
                                userListToRemove.add(user);
                    }
                }
                classUsers.removeAll(userListToRemove);
                System.out.println("Size of User"+classUsers.size());
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Set onClickListener
    private void setOnClickListener() {
        listener = new StudentAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                menu.add(position,0,0,"Edit");
                menu.add(position,1,1,"Delete");
            }

            @Override
            public void onTouch(View v, int position) {

            }
        };
    }


    //When add student to class we will add student to enrollment.This function reslove this problem
    public void AddStudentToClass(ClassModel1 classModel1,User user)
    {
        Enrollment enrollment=new Enrollment();
        enrollment.setClassId(classModel1.getClassId());
        enrollment.setClassName(classModel1.getClassName());
        enrollment.setClassRoom(classModel1.getRoom());
        enrollment.setStudentId(user.getUserId());
        enrollment.setClassTuition(classModel1.getTuition());
        enrollment.setStudentCode(user.getEmail().substring(0,8));
        enrollment.setStudentName(user.getName());
        enrollment.setFinalScore(-1);
        enrollment.setMidScore(-1);
        String fulldate=classModel1.getStartDate()+" | Từ tiết "+classModel1.getStartTime()+" - "+classModel1.getEndTime();
        //enrollment.setStudentCode(user.getEmail().substring(0,8));
//        String fulldate=classModel1.getStartDate()+" | "+changeTime(classModel1.getStartTime())+" - "+changeTime(classModel1.getEndTime());
        enrollment.setFullDate(fulldate);
        enrollment.setStateMark(0);
        String key;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Enrollments").child(classModel1.getSemesterId());
        key = mDatabase.push().getKey();
        enrollment.setId(key);
        mDatabase.child(key).setValue(enrollment);
        AddStudentToAttendce(enrollment,classModel1);
    }


    //When add studnet to enrollment we will add student to class attendenes/
    public void AddStudentToAttendce(Enrollment enrollment,ClassModel1 classModel1)
    {
        List<Lesson> lessonList=new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Lessons").child(classModel1.getSemesterId());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lessonList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Lesson lesson= dataSnapshot.getValue(Lesson.class);
                    if(lesson.getClassId().equals(classModel1.getClassId())) {
                        lessonList.add(lesson);
                    }
                }
                Date date=convertStringToDate(classModel1.getStartDate());
                Calendar calendar=dateToCalendar(date);
                calendar.setTime(date);
                for(Lesson lesson:lessonList)
                {
                    Date dateAdd=calendar.getTime();
                    String dateAddString=convertDateToString(dateAdd);

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Attendances").child(classModel1.getSemesterId());
                    Attendance attendance=new Attendance();
                    attendance.setClassId(classModel1.getClassId());
                    attendance.setStudentId(enrollment.getStudentId());
                    attendance.setStudentName(enrollment.getStudentName());
                    attendance.setLessonId(lesson.getLessonId());
                    attendance.setLessonName(lesson.getName());
                    attendance.setClassName(classModel1.getClassName());
                    attendance.setClassRoom(classModel1.getRoom());
                    attendance.setState(Common.ATTENDANCE_NOT_YET);
                    String fulltime="Tiết "+classModel1.getStartTime()+" - "+classModel1.getEndTime();
                    attendance.setFullDate(lesson.getDate());
                    attendance.setFullTime(fulltime);
                    String key = mDatabase.push().getKey();
                    attendance.setId(key);
                    mDatabase.child(key).setValue(attendance);
                    calendar.add(Calendar.DATE,7);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //This function to load student enrollment class.
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

    @Override
    public void onClick(View v) {

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
