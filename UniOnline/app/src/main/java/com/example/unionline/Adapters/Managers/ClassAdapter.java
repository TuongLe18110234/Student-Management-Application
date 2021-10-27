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
//
//import com.example.implementproject.R;
//import com.example.implementproject.model.ClassModel1;

import com.example.unionline.DTO.ClassModel1;
import com.example.unionline.R;
import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    Context context;
    ArrayList<ClassModel1> listClass;
    private RecyclerViewClickListener listener;
    public ClassAdapter(Context context, ArrayList<ClassModel1> listClass, RecyclerViewClickListener listener)
    {
        this.context = context;
        this.listClass = listClass;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_mananger_class,parent,false);
        return new ViewHolder(view);
    }
    //    public void setData(ArrayList<ClassModel> list){
//        this.listClass = list;
//        notifyDataSetChanged();
//    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassModel1 classModel=listClass.get(position);
        holder.tvClassID.setText(classModel.getClassId());
        holder.tvClassName.setText(classModel.getClassName());
        holder.tvTeacher.setText(classModel.getTeacherId());
        holder.tvCapacity.setText(String.valueOf(classModel.getCapacity()));
        holder.tvSemesterID.setText(classModel.getSemesterId());
        if(classModel.isActive())
        {
            holder.tvActive.setText("Đang hoạt động");
        }
        else {
            holder.tvActive.setText("Đã bị hủy");
        }
        holder.tvRoom.setText(classModel.getRoom());
        holder.tvTimeFrom.setText(classModel.getStartTime());
        holder.tvTimeTo.setText(classModel.getEndTime());
        holder.tvDateStart.setText(classModel.getStartDate());
        holder.tvDateEnd.setText(classModel.getEndDate());
    }

    @Override
    public int getItemCount() {
        return listClass.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        TextView tvClassID,tvClassName,tvTeacher,tvCapacity,tvSemesterID,tvActive,tvRoom,tvDateStart,tvDateEnd,tvTimeFrom,tvTimeTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemClass;
            tvClassID = itemView.findViewById(R.id.tvMaHocPhan);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvTeacher=itemView.findViewById(R.id.tvGiangVien);
            tvCapacity=itemView.findViewById(R.id.tvSiSo);
            tvSemesterID=itemView.findViewById(R.id.tvHocKi);
            tvActive=itemView.findViewById(R.id.tvDateEnd);
            tvRoom=itemView.findViewById(R.id.tvRoom);
            tvTimeFrom=itemView.findViewById(R.id.tvTimeStart);
            tvTimeTo=itemView.findViewById(R.id.tvTimeEnd);
            tvDateStart=itemView.findViewById(R.id.tvDateStart);
            tvDateEnd=itemView.findViewById(R.id.tvDateEnd);


            itemView.setOnClickListener(this);
            itemClass=itemView.findViewById(R.id.item_class);
            itemClass.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View v) {

            listener.onClick(itemView, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            listener.onCreateContextMenu(menu, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onTouch(itemView, getAdapterPosition());
            return true;
        }
    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

        void onCreateContextMenu(ContextMenu menu, int position);

        void onTouch(View v, int position);
    }
}
