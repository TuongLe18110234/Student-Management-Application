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

//import com.example.implementproject.R;
//import com.example.implementproject.model.Notification;
import com.example.unionline.DTO.News;
import com.example.unionline.R;
import com.example.unionline.DTO.Notification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<News> newsArrayList;
    private RecyclerViewClickListener listener;

    public NotificationAdapter(Context context, ArrayList<News> newsArrayList, RecyclerViewClickListener listener) {
        this.context = context;
        this.newsArrayList = newsArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manager_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news=newsArrayList.get(position);
        holder.tvIdNotify.setText(news.getId());
        holder.tvContentNotify.setText(news.getContent());
        holder.tvSentFromNotify.setText(news.getSentFrom());
        holder.tvTitleNotify.setText(news.getTitle());
        holder.tvDateCreate.setText(news.getCreateDate());
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnTouchListener {
        TextView tvIdNotify,tvTitleNotify,tvContentNotify,tvSentToNotify,tvSentFromNotify,tvDateCreate;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemNotification;
            tvIdNotify=itemView.findViewById(R.id.tvIdNotify);
            tvTitleNotify=itemView.findViewById(R.id.tvTitle);
            tvContentNotify=itemView.findViewById(R.id.tvContent);
            tvSentFromNotify=itemView.findViewById(R.id.tvSentFrom);
            tvDateCreate=itemView.findViewById(R.id.tvDateCreateNotify);
            itemView.setOnClickListener(this);
            itemNotification=itemView.findViewById(R.id.item_news);
            itemNotification.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView,getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            listener.onCreateContextMenu(menu,getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            listener.onTouch(itemView,getAdapterPosition());
            return false;
        }
    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);

        void onCreateContextMenu(ContextMenu menu, int position);

        void onTouch(View v, int position);
    }
}
