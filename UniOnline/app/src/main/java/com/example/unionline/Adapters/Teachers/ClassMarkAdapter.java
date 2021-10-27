package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

import java.util.ArrayList;

public class ClassMarkAdapter extends RecyclerView.Adapter<ClassMarkAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Enrollment> scoreList;
    private ClassMarkAdapter.RecyclerViewClickListener listener;

    public ClassMarkAdapter(Context context, ArrayList<Enrollment> scoreList, ClassMarkAdapter.RecyclerViewClickListener listener) {
        this.context = context;
        this.scoreList = scoreList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_update_mark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Enrollment score = scoreList.get(position);

        holder.tvStudentId.setText(score.getStudentCode());
        holder.tvStudentName.setText(score.getStudentName());
        if (score.getMidScore() != -1) {
            holder.edMidTermScore.setText(String.valueOf(score.getMidScore()));
        } else {
            holder.edMidTermScore.setText("");
        }

        if (score.getFinalScore() != -1) {
            holder.edFinalScore.setText(String.valueOf(score.getFinalScore()));
        } else {
            holder.edFinalScore.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener {

        TextView tvStudentId, tvStudentName;
        TextView edMidTermScore, edFinalScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStudentId     = (TextView) itemView.findViewById(R.id.txtId);
            tvStudentName   = (TextView) itemView.findViewById(R.id.txtName);
            edMidTermScore  = (TextView) itemView.findViewById(R.id.edMidTerm);
            edFinalScore    = (TextView) itemView.findViewById(R.id.edFinal);

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
