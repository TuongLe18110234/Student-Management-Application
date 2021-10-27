package com.example.unionline.Views.Manager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Adapters.Managers.ClassAdapter;
import com.example.unionline.DAO.AttendanceDAO;
import com.example.unionline.DAO.ClassDAO;
import com.example.unionline.DAO.EnrollmentDAO;
import com.example.unionline.DAO.LessonDAO;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.DTO.Semester;
import com.example.unionline.DTO.User;
import com.example.unionline.R;
//import com.example.implementproject.DAO.ClassDAO;
//import com.example.implementproject.R;
//import com.example.implementproject.adapter.ClassAdapter;
//import com.example.implementproject.model.ClassModel1;
//import com.example.implementproject.model.Lesson;
//import com.example.implementproject.model.Semester;
import com.example.unionline.DTO.ClassModel1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManageClassControllers extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    ArrayList<ClassModel1> classModels;
    ClassAdapter classAdapter;
    TextView tvClassID,tvClassName,tvDateStart,tvDateEnd,textView2;
    Button btAddClass;
    DatabaseReference mDatabase;
    EditText tvRoom;
    ImageButton btnOpen,ButtonNotification;
    Button btAdd,btClose,btSetDateStart,btSetDateEnd;
    public int maxId;
    private ClassAdapter.RecyclerViewClickListener listener;
    Spinner  spTeacher,spCapacity,spSemester,spTimeFrom,spTimeTo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        View root=inflater.inflate(R.layout.fragment_manager_class,container,false);
        setOnClickListener();
        setRecyclerView(root);
        btnOpen =  root.findViewById(R.id.imageButton2);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenNoteDialog(true,-1);
                setRecyclerView(root);
            }

        });
        ButtonNotification=root.findViewById(R.id.imgButtonNotification);
        ButtonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                ManageNotificationControllers notificationControllers=new ManageNotificationControllers();
                fragmentTransaction.replace(R.id.content_frame,notificationControllers);
                fragmentTransaction.addToBackStack(ManageStudentControllers.TAG);
                fragmentTransaction.commit();

            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                OpenNoteDialog(false,direction);
                classAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
        return root;
    }
    

    @Override
    public void onClick(View v) {

    }
    private void setOnClickListener()
    {
        listener=new ClassAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                OpenNoteDialog(false,position);

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {
                menu.add(position,0,0,"Add student to class");
                menu.add(position,1,1,"Detail class");
                menu.add(position,2,2,"Delete");
            }

            @Override
            public void onTouch(View v, int position) {
//                Intent intent=new Intent(getContext(),ManageStudentControllers.class);
//                startActivity(intent);
//                FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
//                ManageStudentControllers manageStudentControllers=new ManageStudentControllers();
//                Bundle bundle=new Bundle();
//                fragmentTransaction.addToBackStack()

            }
        };


    }
    private void setRecyclerView(View root)
    {
        recyclerView=root.findViewById(R.id.recyclerViewChiTietMonHoc);
        classModels=new ArrayList<>();

        classAdapter=new ClassAdapter(getContext(),classModels,listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(classAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("Classes").child("2020_2021_HK1");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classModels.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ClassModel1 classModel= dataSnapshot.getValue(ClassModel1.class);

                    classModels.add(classModel);

                }
                classAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //Create dialog to add and update
    public void OpenNoteDialog(boolean isAddNew,@Nullable int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.fragment_manager_addclass, null);
        AlertDialog alertDialog = builder.create();

        alertDialog.setView(view);
        alertDialog.show();
        btAdd = view.findViewById(R.id.btAdd);
        btClose = view.findViewById(R.id.btClose);
        tvClassID = view.findViewById(R.id.edtClassID);
        tvClassName = view.findViewById(R.id.editTextClassName);
        spTeacher = view.findViewById(R.id.spTeacher);
        spCapacity = view.findViewById(R.id.spCapacity);
        spSemester = view.findViewById(R.id.spHocKi);
        spTimeFrom = view.findViewById(R.id.spTimeStart);
        spTimeTo = view.findViewById(R.id.spTimeEnd);
        tvDateStart = view.findViewById(R.id.textDateFrom);
        tvDateEnd = view.findViewById(R.id.textDateTo);
        tvRoom = view.findViewById(R.id.editTextRoom);
        btSetDateStart = view.findViewById(R.id.btDateStart);
        btSetDateEnd = view.findViewById(R.id.btDateEnd);

        if (isAddNew == false) {
//            btSetDateStart.setVisibility(View.INVISIBLE);
//            btSetDateEnd.setVisibility(View.INVISIBLE);
            textView2 = view.findViewById(R.id.textView2);
            textView2.setText("Cập nhập lớp học");
            textView2.setTextSize(30);
            ClassModel1 classModel = classModels.get(position);
            tvClassID = view.findViewById(R.id.edtClassID);
            tvClassID.setEnabled(false);
            tvClassID.setText(classModel.getClassId());
            tvClassName = view.findViewById(R.id.editTextClassName);
            tvClassName.setText(classModel.getClassName());
            tvRoom = view.findViewById(R.id.editTextRoom);
            tvRoom.setText(classModel.getRoom());
            tvDateStart.setText(classModel.getStartDate());
            tvDateEnd.setText(classModel.getEndDate());
            ChooseTime(view, classModel.getStartTime(), classModel.getEndTime());
            ChooseTeacher(view, classModel.getTeacherId());
            ChooseCapacity(view, String.valueOf(classModel.getCapacity()));
            ChooseSemester(view, classModel.getSemesterId());
        } else {
            ChooseTeacher(view, "18110092");
            ChooseCapacity(view, "30");
            ChooseSemester(view, "2020_2021_HK1");
            ChooseTime(view, "1", "3");
        }
        btSetDateStart = view.findViewById(R.id.btDateStart);
        btSetDateStart.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  ChooseDateStart(view);
                                              }
                                          }
        );
        btSetDateEnd = view.findViewById(R.id.btDateEnd);
        btSetDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDateEnd(view);
            }
        });
        btClose = view.findViewById(R.id.btClose);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = true;
                String message = "";
                String classId = tvClassID.getText().toString();
                String className = tvClassName.getText().toString();
                String teacher = spTeacher.getSelectedItem().toString();
                String capacity = spCapacity.getSelectedItem().toString();
                String semester = spSemester.getSelectedItem().toString();
                String room = tvRoom.getText().toString();
                String timeStart = spTimeFrom.getSelectedItem().toString();
                String timeEnd = spTimeTo.getSelectedItem().toString();
                String dateStart = tvDateStart.getText().toString();
                String dateEnd = tvDateEnd.getText().toString();
                String state = "Active";
                Boolean status = true;
                if (isAddNew == true) {
                    ClassModel1 classModel = new ClassModel1(classId, semester, teacher, className, Integer.parseInt(capacity), room, timeStart, timeEnd, dateStart, dateEnd, state, status);
                    classModel.setTuition(2352000);
                    error = validationDate(classId, className, room, dateStart, dateEnd, message);
                    if (!error) {
                        ClassDAO.getInstance().setValude(classModel);
                        AddLessonForClass(classModel);
                    }


                } else {
//                    ClassModel classModel=classModels.get(position);
//                    ClassDAO.getInstance().deleteClass(classModel.getClassId());
//                    //classModel.setClassId(classId);
//                    classModel.setClassInfor(classDate);
//                    classModel.setStart(Integer.parseInt(timeFrom));
//                    classModel.setEnd(Integer.parseInt(timeTo));
//                    classModel.setClassCapacity(Integer.parseInt(capacity));
//                    classModel.setClassTeacher(teacher);
//                    classModel.setDateStart(dateStart);
//                    classModel.setDateStart(dateEnd);
//
//                    ClassDAO.getInstance().setClassValue(classModel);
                    ClassModel1 classModel1 = new ClassModel1(classId, semester, teacher, className, Integer.parseInt(capacity), room, timeStart, timeEnd, dateStart, dateEnd, state, status);
                    //error=validationDate(classId,className,room,dateStart,dateEnd,message);
                    error = false;
                    if (!error) {
                        if (ClassDAO.getInstance().deleteClass(classModel1)) {

                            Toast.makeText(getContext(), "Bạn đã cập nhật thành công", Toast.LENGTH_SHORT).show();
                            ClassDAO.getInstance().setValude(classModel1);
                        }
                        //AddLessonForClass(classModel);
                    } else {
                        error = validationDate(classId, className, room, dateStart, dateEnd, message);
                        if (!error) {
                            ClassModel1 classModelOld = classModels.get(position);
                            ClassModel1 classModel = classModelOld;
                            classModel.setClassId(classId);
                            classModel.setClassName(className);
                            classModel.setTeacherId(teacher);
                            classModel.setCapacity(Integer.parseInt(capacity));
                            classModel.setSemesterId(semester);
                            classModel.setRoom(room);
                            classModel.setStartTime(timeStart);
                            classModel.setEndTime(timeEnd);
                            classModel.setStartDate(dateStart);
                            classModel.setEndDate(dateEnd);

                            try {

                                //Update Class
                                ClassDAO.getInstance().setValude(classModel);
                                //Update Enrollment
                                EnrollmentDAO.getInstance().UpdateEnrollmentByClassModel(classModelOld, classModel);
                                //Update lesson
                                LessonDAO.getInstance().UpdateLessonByClassModel(classModelOld, classModel);
                                //Update
                                AttendanceDAO.getInstance().UpdateAttendenceByClassModel(classModelOld, classModel);


                                message = "Bạn đã cập nhật thành công";
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();


                            } catch (Exception e) {

                            }
                        }
                    }

                }
                if (!error)
                    alertDialog.cancel();
            }
        });
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getGroupId();
        switch (item.getItemId()){
            case 0:
                ClassModel1 classModel1=classModels.get(position);
//                Intent intent=new Intent(getContext(),ManageStudentControllers.class);
//                intent.putExtra("classChose",classModel1);
//                startActivity(intent);
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                ManageStudentControllers manageStudentControllers=new ManageStudentControllers();
                Bundle bundle=new Bundle();
                bundle.putSerializable("classChose",classModel1);
                manageStudentControllers.setArguments(bundle);
                fragmentTransaction.replace(R.id.manager_container,manageStudentControllers);
                fragmentTransaction.addToBackStack(ManageStudentControllers.TAG);
                fragmentTransaction.commit();

                return true;
            case 1:
                ClassModel1 classModel=classModels.get(position);
                FragmentTransaction fragmentTransaction1=getFragmentManager().beginTransaction();
                ManagerClassDetail managerClassDetail=new ManagerClassDetail();
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("classChose",classModel);
                managerClassDetail.setArguments(bundle1);
                fragmentTransaction1.replace(R.id.manager_container,managerClassDetail);
                fragmentTransaction1.addToBackStack(ManageStudentControllers.TAG);
                fragmentTransaction1.commit();
                return true;
            case 2 :
                ClassModel1 classModel2 = classModels.get(position);
                List<Lesson> lessons=new ArrayList<>();
                //lessons= LessonDAO.getInstance().getAllLessonByClassModel(classModel2);
                System.out.println("Size of lesson "+lessons.size());
                String message="Bạn đã delete "+String.valueOf(lessons.size());
                //Delete Class
                ClassDAO.getInstance().deleteClass(classModel2);
                //Delete lesson
                LessonDAO.getInstance().DeleteAllLessonByClassModel(classModel2);
                //Delete EnrollMent
                EnrollmentDAO.getInstance().DeleteAllAllEnrollMentByClassModel(classModel2);
                //Delete Attendances
                AttendanceDAO.getInstance().DeleteAllAllAtendenceByClassModel(classModel2);

                 message="Bạn đã xóa lớp học!!!!";
                Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();

        }
        return super.onContextItemSelected(item);
    }

    public void ChooseTime(View view, String timeFrom,String timeTo){
        //Category
        spTimeFrom=(Spinner) view.findViewById(R.id.spTimeStart);
        spTimeTo=(Spinner)view.findViewById(R.id.spTimeEnd);
        //String[] initDate = {"Select category..."};
        List<String> list = new ArrayList<String>();
        for(int i=1;i<17;i++)
        {
            list.add(String.valueOf(i));
        }


        //List<String> listDates = Arrays.asList(listDate);
        Object[] objecTime = list.toArray();

        ArrayAdapter adapterDate = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, objecTime);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTimeFrom.setAdapter(adapterDate);
        spTimeTo.setAdapter(adapterDate);
        spTimeFrom.setSelection(adapterDate.getPosition(timeFrom));
        spTimeTo.setSelection(adapterDate.getPosition(timeTo));
    }
    public void ChooseTeacher(View view, String teacher)
    {
        spTeacher=(Spinner) view.findViewById(R.id.spTeacher);
        String[] listTeacher = {"18110092","18110132","18110234","T1811000"};

        List<String> listTeachers = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTeachers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getRole()==2)
                        listTeachers.add(user.getUserId());
                }
                Object[] objecTeacher = listTeachers.toArray();

                ArrayAdapter adapterDate = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, objecTeacher);
                adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spTeacher.setAdapter(adapterDate);
                spTeacher.setSelection(adapterDate.getPosition(teacher));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ChooseSemester(View view, String semester){
        //Semester

        spSemester=(Spinner) view.findViewById(R.id.spHocKi);
        List<String> listSemsesters = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Semesters");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSemsesters.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Semester semester1 = dataSnapshot.getValue(Semester.class);
                    listSemsesters.add(semester1.getSemesterId());
                }
                Object[] objecTime = listSemsesters.toArray();
                ArrayAdapter adapterDate = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, objecTime);
                adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSemester.setAdapter(adapterDate);
                spSemester.setSelection(adapterDate.getPosition(semester));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //List<String> listSemesterObj = Arrays.asList(listSemsesters );

    }
    public void ChooseCapacity(View view, String capacity){
        //Category

        spCapacity=(Spinner) view.findViewById(R.id.spCapacity);
        //String[] initDate = {"Select category..."};
        List<String> list = new ArrayList<String>();
        for(int i=15;i<45;i++)
        {
            list.add(String.valueOf(i));
        }
        //List<String> listDates = Arrays.asList(listDate);
        Object[] objecTime = list.toArray();

        ArrayAdapter adapterDate = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, objecTime);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCapacity.setAdapter(adapterDate);
        spCapacity.setSelection(adapterDate.getPosition(capacity));
    }
    public void ChooseDateStart(View view)
    {
        tvDateStart=view.findViewById(R.id.textDateFrom);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar_date = Calendar.getInstance();
                calendar_date.set(year,month,dayOfMonth);

                String date = DateFormat.format("dd/MM/yyyy", calendar_date).toString();

                tvDateStart.setText(date);
            }
        }, year, month, day);
        datePickerDialog.setTitle("Select the date");

        datePickerDialog.show();
    }
    public void ChooseDateEnd(View view)
    {
        tvDateEnd=view.findViewById(R.id.textDateTo);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar_date = Calendar.getInstance();
                calendar_date.set(year,month,dayOfMonth);

                String date = DateFormat.format("dd/MM/yyyy", calendar_date).toString();

                tvDateEnd.setText(date);
            }
        }, year, month, day);
        datePickerDialog.setTitle("Select the date");

        datePickerDialog.show();
    }
    // Add lesson for Class
    public void AddLessonForClass(ClassModel1 classModel1)
    {
        String key;
        String name="Lesson 1";
        Date date=convertStringToDate(classModel1.getStartDate());
        Calendar calendar=dateToCalendar(date);
        calendar.setTime(date);
        for(int i=1;i<16;i++)
        {
            Date dateAdd=calendar.getTime();
            String dateAddString=convertDateToString(dateAdd);
            Lesson lesson = new Lesson();

            lesson.setName(name);
            lesson.setClassId(classModel1.getClassId());
            lesson.setWeek(i);
            lesson.setStatus(false);
            String fulldate=dateAddString+" | "+"Tiết "+classModel1.getStartTime()+" - "+classModel1.getEndTime();
            lesson.setDate(dateAddString);
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Lessons").child(classModel1.getSemesterId());
            key = mDatabase.push().getKey();
            lesson.setLessonId(key);
            mDatabase.child(key).setValue(lesson);
            name="Lesson "+String.valueOf(i+1);
            calendar.add(Calendar.DATE,7);
        }
    }
    public boolean validationDate(String mahocphan,String name,String room,String dateStart,String dateEnd,String message)
    {

        boolean error=false;
        List<ClassModel1> classModelListCheck=new ArrayList<>();
        getList(classModelListCheck);
        if(mahocphan.trim().equals("")||name.trim().equals("")||room.trim().equals("")||dateStart.equals("")||dateEnd.equals(""))
        {
            //System.out.println("Null value");
            message="Null value";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            error = true;
        }
        else {
            if(dateStart.equals("start")||dateEnd.equals("end"))
            {
                message="Bạn nên chọn thời gian môn học";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                error = true;
            }
            else {
                Date dateStartValue = convertStringToDate(dateStart);
                Date dateEndValue = convertStringToDate(dateEnd);
                Date dateNow = Calendar.getInstance().getTime();
                long numberDate = (dateEndValue.getTime() - dateStartValue.getTime()) / (24 * 3600 * 1000);
                long numberDateNow = dateStartValue.getTime() - dateNow.getTime();
                System.out.println(String.valueOf(numberDate));

                System.out.println("Number of list" + classModels.size());
                for (ClassModel1 model1 : classModels) {
                    if (model1.getClassId().equals(mahocphan)) {
                        message = "Mã học phần này đã tồn tại trong bảng";
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        error = true;
                    }
                }

                if (numberDate < 105) {
                    message = "Bạn nên chọn thời gian đủ 15 tuần";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    error = true;
                } else if (numberDateNow < 0) {
                    message = "Bạn nên chọn thời gian lớn hơn thời gian hiện tại";
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    System.out.print("Bạn nên chọn thời gian lớn hơn thời gian hiện tại");
                    error = true;
                }

            }
        }
        return error;
    }
    public void getList(List<ClassModel1> classModelListCheck)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Classes").child("2020_2021_HK1");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classModelListCheck.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ClassModel1 classModel = dataSnapshot.getValue(ClassModel1.class);
                    classModelListCheck.add(classModel);

                }
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
}
