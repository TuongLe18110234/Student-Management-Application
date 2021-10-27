package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Common;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.R;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    Context context;
    ArrayList<AbsenceApplication> applications;
    RecyclerViewClickListener listener;

    public ApplicationAdapter(Context context, ArrayList<AbsenceApplication> applications, RecyclerViewClickListener listener) {
        this.context = context;
        this.applications = applications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ViewHolder holder, int position) {
        AbsenceApplication application = applications.get(position);

        int pendingColor = context.getColor(R.color.text_blue_color);
        int approveColor = context.getColor(R.color.successColor);
        int refuseColor = context.getColor(R.color.errorColor);

        String studentId = application.getStudentId();
        String studentCode = application.getStudentCode();
        String studentName = application.getStudentName();
        int status = application.getState();

        String strStatus;
        switch (status) {
            case Common.AA_WAIT_FOR_APPROVAL:
                strStatus = "Đang chờ";
                holder.txtState.setTextColor(pendingColor);
                break;
            case Common.AA_APPROVED:
                strStatus = "Chấp nhận";
                holder.txtState.setTextColor(approveColor);
                break;
            case Common.AA_NOT_APPROVED:
                strStatus = "Từ chối";
                holder.txtState.setTextColor(refuseColor);
                break;
            default:
                strStatus = "";
                break;
        }

        holder.txtStudentId.setText(studentCode);
        holder.txtStudentName.setText(studentName);

        holder.txtState.setText(strStatus);
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView txtStudentId, txtStudentName, txtState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStudentId = itemView.findViewById(R.id.txtStudentId);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            txtState = itemView.findViewById(R.id.txtStatus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCLick(itemView, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onTouch(itemView, getAdapterPosition());
            return true;
        }
    }

    public interface RecyclerViewClickListener {
        void onCLick(View v, int position);

        void onTouch(View v, int position);
    }
}