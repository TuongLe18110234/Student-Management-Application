package com.example.unionline.Adapters.Students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Common;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

import java.util.ArrayList;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder> {

    Context context;
    ArrayList<Enrollment> enrollments;
    RecyclerViewClickListener listener;

    public MarkAdapter(Context context, ArrayList<Enrollment> enrollments, RecyclerViewClickListener listener) {
        this.context = context;
        this.enrollments = enrollments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_mark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enrollment enrollment = enrollments.get(position);

        holder.tvClassName.setText(enrollment.getClassName());
        if(enrollment.getMidScore()!=-1) {
            holder.tvMidMark.setText(String.valueOf(enrollment.getMidScore()));
        }
        if(enrollment.getFinalScore()!=-1){
            holder.tvFinalMark.setText(String.valueOf(enrollment.getFinalScore()));
        }
    }

    @Override
    public int getItemCount() {
        return enrollments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvClassName, tvMidMark, tvFinalMark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvMidMark = itemView.findViewById(R.id.tvMidMark);
            tvFinalMark = itemView.findViewById(R.id.tvFinalMark);

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
