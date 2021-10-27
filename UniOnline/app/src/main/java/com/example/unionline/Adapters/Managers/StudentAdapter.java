package com.example.unionline.Adapters.Managers;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.unionline.R;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.implementproject.R;
//import com.example.implementproject.model.User;

import com.example.unionline.DTO.User;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    TextView tvClassID,tvClassName,tvTeacher,tvCapacity;
    Context context;
    ArrayList<User> listUser;
    private RecyclerViewClickListener listener;
    public StudentAdapter(Context context, ArrayList<User> listUser, RecyclerViewClickListener listener)
    {
        this.context = context;
        this.listUser = listUser;
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
        User user=listUser.get(position);
        holder.tvNameID.setText(user.getUserId());
        holder.tvNameStudent.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener{
        TextView tvNameID,tvNameStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemStudent;
            tvNameID=itemView.findViewById(R.id.textView_hovaten);
            tvNameStudent=itemView.findViewById(R.id.textView_tentaikhoan);


            itemView.setOnClickListener(this);
            itemStudent=itemView.findViewById(R.id.item_students);
            itemStudent.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onClick(itemView,getAdapterPosition());
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
