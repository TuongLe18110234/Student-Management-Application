package com.example.unionline.Adapters.Managers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;

import java.util.ArrayList;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {

    Context context;
    ArrayList<Lesson> lessons;
    RecyclerViewClickListener listener;

    public LessonAdapter(Context context, ArrayList<Lesson> lessons, RecyclerViewClickListener listener) {
        this.context = context;
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        String week = "Tuần " + String.valueOf(lesson.getWeek()) + " - " + lesson.getDate();
        holder.tvLessonWeek.setText(week);
        holder.tvLessonName.setText(lesson.getName());

        if(lesson.isStatus()) {
            holder.tvLessonState.setText("Đã diễn ra");
        } else {
            holder.tvLessonState.setText("Chưa diễn ra");
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvLessonName, tvLessonWeek, tvLessonState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvLessonWeek = itemView.findViewById(R.id.tvLessonWeek);
            tvLessonState = itemView.findViewById(R.id.tvLessonState);

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
