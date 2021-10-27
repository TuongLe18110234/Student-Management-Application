package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Common;
import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{
    Context context;
    ArrayList<Lesson> lessons;
    ArrayList<Class> classes;
    DatabaseReference mDatabase;

    public ScheduleAdapter(Context context, ArrayList<Lesson> lessons, ArrayList<Class> classes) {
        this.context = context;
        this.lessons = lessons;
        this.classes = classes;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, parent,false);

        return new ScheduleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ScheduleAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        Optional<Class> aClass = classes.stream().parallel().filter(c -> c.getClassId() == lesson.getClassId()).findFirst();
        if (aClass.isPresent()) {
            holder.tvClassName.setText(aClass.get().getClassName());
            holder.tvRoom.setText(aClass.get().getRoom());
            holder.tvTime.setText(lesson.getDate() + " tiết " + aClass.get().getStartTime() + " - " + aClass.get().getEndTime());
        }

        holder.tvClassId.setText(lesson.getClassId());
        holder.tvLessonName.setText(lesson.getName());

        int greenColor = context.getColor(R.color.successColor);
        int redColor = context.getColor(R.color.errorColor);

        if(lesson.isStatus() == true) {
            holder.tvStatus.setText("Đã học");
            holder.tvStatus.setTextColor(greenColor);
        } else {
            holder.tvStatus.setText("Chưa học");
            holder.tvStatus.setTextColor(redColor);
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvClassName, tvClassId, tvRoom, tvTime, tvStatus, tvLessonName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.txtClassName);
            tvClassId = itemView.findViewById(R.id.txtClassId);
            tvRoom = itemView.findViewById(R.id.txtRoom);
            tvTime = itemView.findViewById(R.id.txtDate);
            tvStatus = itemView.findViewById(R.id.txtStatus);
            tvLessonName = itemView.findViewById(R.id.txtLessonName);
        }
    }
}
