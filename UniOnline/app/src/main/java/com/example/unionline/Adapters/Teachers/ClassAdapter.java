package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Class;
import com.example.unionline.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    Context context;
    ArrayList<Class> classes;
    RecyclerViewClickListener listener;

    public ClassAdapter(Context context, ArrayList<Class> classes, RecyclerViewClickListener listener) {
        this.context = context;
        this.classes = classes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position) {
        Class aClass = classes.get(position);

        String className = aClass.getClassName();
        String classDate = "Từ ngày " + aClass.getStartDate() + " đến ngày " + aClass.getEndDate();
        holder.txtClassName.setText(className);
        holder.txtClassDate.setText(classDate);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        TextView txtClassName, txtClassDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtClassDate = itemView.findViewById(R.id.txtClassDate);

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
