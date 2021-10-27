package com.example.unionline.Adapters.Students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.News;
import com.example.unionline.DTO.Notification;
import com.example.unionline.R;

import java.util.ArrayList;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    Context context;
    ArrayList<News> newses;

    private RecyclerViewClickListener listener;


    public NewsAdapter(Context context, ArrayList<News> newses, RecyclerViewClickListener listener){
        this.context = context;
        this.newses = newses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_student_news, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news = newses.get(position);

        holder.ivImageNews.setImageResource(R.drawable.new_image);
        holder.tvSentTo.setText(news.getSentTo());
        holder.tvSendDate.setText(news.getCreateDate());
        holder.tvTitle.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivImageNews;
        TextView tvSentTo, tvSendDate, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImageNews = itemView.findViewById(R.id.ivImageNews);
            tvSentTo = itemView.findViewById(R.id.tvSentTo);
            tvSendDate = itemView.findViewById(R.id.tvSendDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
