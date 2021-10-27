package com.example.unionline.Adapters.Students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TuitionAdapter extends RecyclerView.Adapter<TuitionAdapter.ViewHolder> {

    Context context;
    ArrayList<Enrollment> enrollments;
    RecyclerViewClickListener listener;

    NumberFormat formatter;
    double myNumber;
    String formattedNumber;

    public TuitionAdapter(Context context, ArrayList<Enrollment> enrollments, RecyclerViewClickListener listener) {
        this.context = context;
        this.enrollments = enrollments;
        this.listener = listener;

        formatter = new DecimalFormat("#,###");
        myNumber = 1000000;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_tuition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enrollment enrollment = enrollments.get(position);

        holder.tvClassName.setText(enrollment.getClassName());

        //JUST FOR FUN
        formattedNumber = formatter.format(enrollment.getClassTuition());

        holder.tvClassTuition.setText(formattedNumber + " VND");
    }

    @Override
    public int getItemCount() {
        return enrollments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvClassName, tvClassTuition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassTuition = itemView.findViewById(R.id.tvClassTuition);

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
