package com.example.unionline.Views.Manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.implementproject.DAO.NotificationDAO;
//import com.example.implementproject.R;
//import com.example.implementproject.adapter.NotificationAdapter;
//import com.example.implementproject.model.Notification;
import com.example.unionline.Adapters.Managers.ClassAdapter;
import com.example.unionline.Adapters.Managers.NotificationAdapter;
import com.example.unionline.DAO.NewsDAO;
import com.example.unionline.DAO.NotificationDAO;
import com.example.unionline.DTO.ClassModel1;
import com.example.unionline.DTO.News;
import com.example.unionline.DTO.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.example.unionline.R;
public class ManageNotificationControllers extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    ArrayList<News> newsArrayList;
    NotificationAdapter notificationAdapter;
    TextView tvIdNotify,tvTitleNotify,tvSentToNotify,tvSentFromNotify,tvDateCreate;
    ImageButton btAdd,imageButtonBack;
    Button btAddNotify,btClose;
    Spinner spinnerSentTo;
    EditText tvContentNotify;
    private  NotificationAdapter.RecyclerViewClickListener listener;
    DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
    View root=inflater.inflate(R.layout.fragment_manager_notification,container,false);
    setOnClickListener();
    setRecyclerView(root,"");
    imageButtonBack=root.findViewById(R.id.imageButtonBack);
    imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    btAdd=root.findViewById(R.id.imageButton10);
    btAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OpenNotificationDialog(true,-1);
        }
    });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Notification notification=notifications.get(direction);
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                builder.setMessage("Do you want delete notification !!!")
                        // positiveButton là nút thuận : đặt là OK
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                News news=newsArrayList.get(viewHolder.getAdapterPosition());

                                boolean status= NewsDAO.getInstance().deleteNews(news.getId());
                                if(status)
                                {
                                    Toast.makeText(getContext(), "Delete notification completed", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Delete notification failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        // ngược lại negative là nút nghịch : đặt là cancel
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                notificationAdapter.notifyDataSetChanged();
                            }
                        });
                // tạo dialog và hiển thị
                builder.create().show();

                notificationAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);

        return root;
    }


    //Set data for reccyclerView news
    private void setRecyclerView(View root,String sortBy) {
        recyclerView=root.findViewById(R.id.recyclerViewThongBao);
        newsArrayList=new ArrayList<>();
        notificationAdapter=new NotificationAdapter(getContext(),newsArrayList,listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference("News");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                   News news= dataSnapshot.getValue(News.class);
                   if(sortBy.equals(""))
                   {
                       newsArrayList.add(news);
                   }
                   else
                   {
                       if(sortBy.equals(news.getSentTo().toString()))
                           newsArrayList.add(news);
                   }


                }
                System.out.println("Value is"+newsArrayList.size());
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setOnClickListener()
    {
        listener=new NotificationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                OpenNotificationDialog(false,position);

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, int position) {

            }

            @Override
            public void onTouch(View v, int position) {


            }
        };


    }
    public void OpenNotificationDialog(boolean isAddNew,@NonNull int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.fragment_manager_addnotification, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(view);
        alertDialog.show();

        spinnerSentTo=view.findViewById(R.id.spSendTo);
        tvTitleNotify=view.findViewById(R.id.textTieuDe);
        tvContentNotify=view.findViewById(R.id.editContent);
        if(isAddNew==false)
        {

                News news=newsArrayList.get(position);
                ChooseSentTo(view,news.getSentTo());
                tvTitleNotify.setText(news.getTitle());
                tvContentNotify.setText(news.getContent());

        }
        else
        {
            ChooseSentTo(view,"Gửi đến giáo viên");
        }
        btClose=view.findViewById(R.id.button_huy);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btAddNotify=view.findViewById(R.id.button_addthongbao);
        btAddNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error=false;
                String key = mDatabase.push().getKey();
                String notifyTitle=tvTitleNotify.getText().toString();
                String notifyContent=tvContentNotify.getText().toString();
                String notifySentTo=spinnerSentTo.getSelectedItem().toString();
                Date dateNow=java.util.Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String dateCreate=simpleDateFormat.format(dateNow);
                String notifySentFrom="Quản lý khoa";
                error=validationNews(notifyTitle,notifyContent);
                if(isAddNew)
                {

                    if(!error) {
                        News news = new News(key, notifyTitle, notifyContent, 0, notifySentFrom, notifySentTo, dateCreate);
                        //NotificationDAO notificationDAO=new NotificationDAO();
                        NewsDAO.getInstance().setValude(news);
                    }

                }
                else
                {
                    if(!error) {
                        News news = newsArrayList.get(position);
                        news.setContent(notifyContent);
                        news.setTitle(notifyTitle);
                        news.setSentTo(notifySentTo);
                        NewsDAO.getInstance().setValude(news);
                    }
                }
                if(!error)
                    alertDialog.cancel();
            }
        });

    }


    @Override
    public void onClick(View v) {

    }
    public void ChooseSentTo(View view, String sendTo)
    {
        spinnerSentTo=(Spinner) view.findViewById(R.id.spSendTo);
        String[] listTeacher = {"Giảng viên","Sinh viên","Phụ huynh","Giảng viên,Sinh viên","Toàn trường"};

        List<String> listTeachers = Arrays.asList(listTeacher);
        Object[] objecTeacher = listTeachers.toArray();

        ArrayAdapter adapterDate = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, objecTeacher);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSentTo.setAdapter(adapterDate);
        spinnerSentTo.setSelection(adapterDate.getPosition(sendTo));
    }
    public boolean validationNews(String title,String content)
    {

        boolean error=false;
        String message;
//        List<ClassModel1> classModelListCheck=new ArrayList<>();
//        getList(classModelListCheck);
        if(title.trim().equals("")||content.trim().equals(""))
        {
            message="Bạn đã nhập giá trị rỗng";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            error = true;
        }

        return error;
    }

}
