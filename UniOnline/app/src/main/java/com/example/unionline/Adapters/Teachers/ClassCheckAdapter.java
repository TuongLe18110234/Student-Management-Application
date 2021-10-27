package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.Class;
import com.example.unionline.R;

import java.util.ArrayList;

public class ClassCheckAdapter extends RecyclerView.Adapter<ClassCheckAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Class> classes;
    private ClassCheckAdapter.RecyclerViewClickListener listener;

    public ClassCheckAdapter(Context context, ArrayList<Class> classes, ClassCheckAdapter.RecyclerViewClickListener listener) {
        this.context = context;
        this.classes = classes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_classes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class aClass = classes.get(position);

        holder.tvClassName.setText(aClass.getClassName());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvClassName;
        CheckBox cbIsChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClassName = (TextView) itemView.findViewById(R.id.txtClassName);
            cbIsChecked = (CheckBox) itemView.findViewById(R.id.cbIsChecked);

            cbIsChecked.setOnClickListener(this);
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