package com.example.unionline;

import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Semester;
import com.example.unionline.DTO.User;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static User user;
    public static Semester semester;
    public static Class class_;

    // User roles
    public static final int roleAdmin = 0;
    public static final int roleManager = 1;
    public static final int roleTeacher = 2;
    public static final int roleStudent = 3;
    public static final int roleParent = 4;
    public static ArrayList<String> userRoles;

    // Attendance states
    public static final int ATTENDANCE_NOT_YET = 0;
    public static final int ATTENDANCE_ON_TIME = 1;
    public static final int ATTENDANCE_LATE = 2;
    public static final int ATTENDANCE_WITH_PERMISSION = 3;
    public static final int ATTENDANCE_WITHOUT_PERMISSION = 4;
    public static ArrayList<String> attendanceNames;

    //Set state for mark
    public static final int MARK_NOT_ENTER = 0; //Mark entered
    public static final int MARK_ENTER = 1; // Mark not yet entered

    //Set state for absence application
    public static final int AA_WAIT_FOR_APPROVAL = 0; //Mark entered
    public static final int AA_APPROVED = 1; // Mark not yet entered
    public static final int AA_NOT_APPROVED = 2; // Mark not yet entered
    public static ArrayList<String> aaNames;

    public Common(){
        semester = new Semester();
        semester.setSemesterId("2020_2021_HK1");
        semester.setSemesterName("Học kỳ 1, năm học 2020-2021");

        /*user = new User();
        user.setUserId("18110234");*/

        //Set user role
        userRoles = new ArrayList<String>();
        userRoles.add("Admin");
        userRoles.add("Quản lý khoa");
        userRoles.add("Giảng viên");
        userRoles.add("Sinh viên");
        userRoles.add("Phụ huynh");

        //Set attendance name
        attendanceNames = new ArrayList<>();
        attendanceNames.add("Chưa điểm danh");
        attendanceNames.add("Đúng giờ");
        attendanceNames.add("Trễ");
        attendanceNames.add("Vắng có phép");
        attendanceNames.add("Vắng không phép");

        //Set  absence application name
        aaNames = new ArrayList<String>();
        aaNames.add("Đang chờ phê duyệt");
        aaNames.add("Đã được phê duyệt");
        aaNames.add("Không phê duyệt");
    }
}