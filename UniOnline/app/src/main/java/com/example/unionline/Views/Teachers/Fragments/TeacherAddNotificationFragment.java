package com.example.unionline.Views.Teachers.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unionline.Common;
import com.example.unionline.DAO.NotificationDAO;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Notification;
import com.example.unionline.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TeacherAddNotificationFragment extends Fragment {

    private static final String ARG_IDS = "ListIDs";
    private static final int SUCCESS = 1;
    private static final int NULL_VALUE = 0;
    private static final int ERROR = -1;

    private String classIds;
    private EditText edTittle, edContent;
    private Button btnSend;
    private TextView fragmentName;
    private ImageView backIcon;

    public TeacherAddNotificationFragment() {
        // Required empty public constructor
    }

    public static TeacherAddNotificationFragment newInstance(String classIds) {
        TeacherAddNotificationFragment fragment = new TeacherAddNotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_IDS, classIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classIds = getArguments().getString(ARG_IDS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_teacher_add_notification, container, false);

        mappingView(root);
        setListenerView();

        return root;
    }

    // Mapping variables with view in layout
    private void mappingView(View root) {
        // Toolbar
        fragmentName = (TextView) root.findViewById(R.id.activity_name);
        fragmentName.setText("Soạn thông báo");
        backIcon = (ImageView) root.findViewById(R.id.left_icon);

        // Body
        edTittle    = (EditText) root.findViewById(R.id.edTittle);
        edContent   = (EditText) root.findViewById(R.id.edContent);
        btnSend     = (Button) root.findViewById(R.id.btnSendNotification);
    }

    /**
     * Set event click for backIcon and button btnSend
     */
    private void setListenerView() {
        // When click on backIcon (on the left of toolbar) --> return TeacherChooseClassFragment
        backIcon.setOnClickListener((View v) -> {
            changeFragment();
        });

        // Validate and send notification
        btnSend.setOnClickListener((View v) -> {
            // If valid (tittle and content not null)
            if(validateNotification()) {
                try {
                    // Send notification and show dialog success
                    sendNotification();
                    openMessageDiagLog(SUCCESS);
                } catch (Exception e) {
                    // If have an exception --> show dialog error
                    openMessageDiagLog(ERROR);
                }
            }
            // If invalid (tittle or content is null
            else {
                openMessageDiagLog(NULL_VALUE);
            }
        });
    }

    /**
     * Validate notification
     * @return true if both tittle and content is not null
     * @return false if tittle or content is null
     */
    private boolean validateNotification() {
        if(edTittle.getText().toString().equals("") || edContent.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Send notification to database
     */
    private void sendNotification() {
        // Create and set data for a Notification object
        Notification notification = new Notification();
        // Tittle and content get from edittext
        notification.setTitle(edTittle.getText().toString());
        notification.setContent(edContent.getText().toString());
        // Send from current user
        notification.setSentFrom(Common.user.getUserId());
        // Send to classIds (classIds get from bundle)
        notification.setSentTo(classIds);

        // CreateDate == current date
        // Change date to string
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date createDate = new Date();
        notification.setCreateDate(formatter.format(createDate));

        // Call NotificationDAO to insert notification to database
        NotificationDAO.getInstance().insertNotification(notification);
    }

    /**
     * Create and show a message dialog when user send notification
     * @param Type: type of message (SUCCESS, NULL_VALUE, ERROR)
     */
    private void openMessageDiagLog(int Type) {
        // Create and set some attributes for dialog
        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.diaglog_notification);
        dialog.setCancelable(false);

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
        ImageView imgIcon = dialog.findViewById(R.id.imgIconType);
        TextView tvMessage = dialog.findViewById(R.id.txtMessage);
        TextView btnClose = dialog.findViewById(R.id.btnClose);

        // Set message content corresponding to type
        if (Type == ERROR) {
            imgIcon.setImageResource(R.drawable.ic_error_outline);
            tvMessage.setText("Gửi thông báo thất bại!");
        } else if (Type == NULL_VALUE) {
            imgIcon.setImageResource(R.drawable.ic_error_outline);
            tvMessage.setText("Vui lòng nhập đầy đủ thông tin!");
        } else if (Type == SUCCESS) {
            imgIcon.setImageResource(R.drawable.ic_success_outline);
            tvMessage.setText("Gửi thông báo thành công!");
        }

        // Dismiss dialog when click btnClose
        btnClose.setOnClickListener((View v) -> {
            dialog.dismiss();
        });

        // Show dialog
        dialog.show();
    }

    /**
     * Replace current fragment with TeacherChooseClassFragment
     */
    private void changeFragment() {
        TeacherChooseClassFragment fragment = new TeacherChooseClassFragment();
        getFragmentManager().beginTransaction().replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }
}
