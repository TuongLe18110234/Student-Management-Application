package com.example.unionline.Adapters.Students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Class;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

import java.util.ArrayList;

public class EnrollmentAdapter extends RecyclerView.Adapter<EnrollmentAdapter.ViewHolder> {

    Context context;
    ArrayList<Enrollment> enrollments;
    RecyclerViewClickListener listener;

    public EnrollmentAdapter(Context context, ArrayList<Enrollment> enrollments, RecyclerViewClickListener listener) {
        this.context = context;
        this.enrollments = enrollments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enrollment enrollment = enrollments.get(position);

        holder.tvClassName.setText(enrollment.getClassName());
        holder.tvClassDate.setText(enrollment.getFullDate());
        holder.tvClassRoom.setText(enrollment.getClassRoom());
    }

    @Override
    public int getItemCount() {
        return enrollments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvClassName, tvClassDate, tvClassRoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassDate = itemView.findViewById(R.id.tvClassDate);
            tvClassRoom = itemView.findViewById(R.id.tvClassRoom);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCLick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onCLick(View v, int position);
    }
}
