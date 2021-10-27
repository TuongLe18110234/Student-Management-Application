package com.example.unionline.Adapters.Managers;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

import java.util.ArrayList;

public class EnrollmentApdapter extends RecyclerView.Adapter<EnrollmentApdapter.ViewHolder>{

    Context context;
    ArrayList<Enrollment> enrollments;
    private RecyclerViewClickListener listener;


    public EnrollmentApdapter(Context context, ArrayList<Enrollment> enrollments, EnrollmentApdapter.RecyclerViewClickListener listener)
    {
        this.context = context;
        this.enrollments = enrollments;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_manager_studenttoclass,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enrollment enrollment=enrollments.get(position);
        holder.tvNameID.setText(enrollment.getStudentId());
        holder.tvNameStudent.setText(enrollment.getStudentName());
    }

    @Override
    public int getItemCount() {
        return enrollments.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener {
        TextView tvNameID, tvNameStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemStudent;
            tvNameID = itemView.findViewById(R.id.textView_hovaten);
            tvNameStudent = itemView.findViewById(R.id.textView_tentaikhoan);


            itemView.setOnClickListener(this);
            itemStudent = itemView.findViewById(R.id.item_students);
            itemStudent.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

        void onCreateContextMenu(ContextMenu menu, int position);

        void onTouch(View v, int position);
    }
}
