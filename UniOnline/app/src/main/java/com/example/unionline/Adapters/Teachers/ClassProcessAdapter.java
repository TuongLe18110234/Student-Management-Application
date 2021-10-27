package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Lesson;
import com.example.unionline.R;

import java.util.ArrayList;

public class ClassProcessAdapter extends RecyclerView.Adapter<ClassProcessAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Lesson> lessons;
    private ClassProcessAdapter.RecyclerViewClickListener listener;

    public ClassProcessAdapter(Context context, ArrayList<Lesson> lessons, ClassProcessAdapter.RecyclerViewClickListener listener) {
        this.context = context;
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_update_class_process, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassProcessAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        holder.tvWeek.setText("Tuần " + lesson.getWeek());
        holder.tvLessonName.setText(lesson.getName());
        holder.cbIsLearned.setChecked(lesson.isStatus());
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView tvWeek, tvLessonName;
        CheckBox cbIsLearned;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWeek          = (TextView) itemView.findViewById(R.id.txtWeek);
            tvLessonName    = (TextView) itemView.findViewById(R.id.txtLessonName);
            cbIsLearned     = (CheckBox) itemView.findViewById(R.id.cbIsLearned);

            cbIsLearned.setOnTouchListener(this);
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

        void onTouch(View v, int position);

        void onCLick(View itemView, int adapterPosition);
    }
}
