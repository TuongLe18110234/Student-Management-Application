package com.example.unionline.Adapters.Students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Common;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.R;

import java.util.ArrayList;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{
    Context context;
    ArrayList<Attendance> listAttendance;

    private RecyclerViewClickListener listener;


    public AttendanceAdapter(Context context, ArrayList<Attendance> listAttendance, RecyclerViewClickListener listener){
        this.context = context;
        this.listAttendance = listAttendance;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_attendance, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        Attendance attendance = listAttendance.get(position);

        holder.tvClassName.setText(attendance.getClassName());
        holder.tvRoom.setText(attendance.getClassRoom());


        if(attendance.getState() == Common.ATTENDANCE_NOT_YET){
            holder.tvState.setText("Chưa học");
        } else {
            holder.tvState.setText("Đã học");
        }

        holder.tvFullTime.setText(attendance.getFullDate()  + " | " + attendance.getFullTime());
        holder.tvAttendanceState.setText(Common.attendanceNames.get(attendance.getState()));
    }

    @Override
    public int getItemCount() {
        return listAttendance.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvClassName, tvRoom, tvState, tvFullTime, tvAttendanceState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvState = itemView.findViewById(R.id.tvState);
            tvFullTime = itemView.findViewById(R.id.tvClassDate);
            tvAttendanceState = itemView.findViewById(R.id.tvAttendanceState);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
