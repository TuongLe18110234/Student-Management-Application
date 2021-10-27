package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Common;
import com.example.unionline.DTO.Attendance;
import com.example.unionline.R;

import java.util.ArrayList;

public class UpdateAttendantAdapter extends RecyclerView.Adapter<UpdateAttendantAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Attendance> attendances;
    private UpdateAttendantAdapter.RecyclerViewClickListener listener;

    public UpdateAttendantAdapter(Context context, ArrayList<Attendance> attendances, UpdateAttendantAdapter.RecyclerViewClickListener listener) {
        this.context = context;
        this.attendances = attendances;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UpdateAttendantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);

        return new UpdateAttendantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateAttendantAdapter.ViewHolder holder, int position) {
        Attendance attendance = attendances.get(position);

        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvStudentName.setText(attendance.getStudentName());

        switch (attendance.getState()) {
            case Common.ATTENDANCE_ON_TIME:
                holder.rbOnTime.setChecked(true);
                break;
            case Common.ATTENDANCE_LATE:
                holder.rbLate.setChecked(true);
                break;
            case Common.ATTENDANCE_WITH_PERMISSION:
                holder.rbWithPermission.setChecked(true);
                break;
            case Common.ATTENDANCE_WITHOUT_PERMISSION:
                holder.rbWithoutPermission.setChecked(true);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNumber, tvStudentName;
        RadioButton rbOnTime, rbLate, rbWithPermission, rbWithoutPermission;
        EditText etNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            tvStudentName = (TextView) itemView.findViewById(R.id.txtStudentName);
            rbOnTime = (RadioButton) itemView.findViewById(R.id.rbOnTime);
            rbLate = (RadioButton) itemView.findViewById(R.id.rbLate);
            rbWithPermission = (RadioButton) itemView.findViewById(R.id.rbWithPermission);
            rbWithoutPermission = (RadioButton) itemView.findViewById(R.id.rbWithoutPermission);
            etNote = (EditText) itemView.findViewById(R.id.etNote);

            rbOnTime.setOnClickListener(this);
            rbLate.setOnClickListener(this);
            rbWithPermission.setOnClickListener(this);
            rbWithoutPermission.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCLick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {

        void onCLick(View itemView, int adapterPosition);
    }
}
