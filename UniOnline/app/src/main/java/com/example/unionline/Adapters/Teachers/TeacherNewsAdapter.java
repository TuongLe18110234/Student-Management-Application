package com.example.unionline.Adapters.Teachers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.News;
import com.example.unionline.R;

import java.util.ArrayList;


public class TeacherNewsAdapter extends RecyclerView.Adapter<TeacherNewsAdapter.ViewHolder>{
    Context context;
    ArrayList<News> newses;

    private RecyclerViewClickListener listener;


    public TeacherNewsAdapter(Context context, ArrayList<News> newses, RecyclerViewClickListener listener){
        this.context = context;
        this.newses = newses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newses.get(position);

        holder.imgNews.setImageResource(R.drawable.new_image);
        holder.tvSentTo.setText(news.getSentTo());
        holder.tvDate.setText(news.getCreateDate());
        holder.tvTittle.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return newses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgNews;
        TextView tvTittle, tvSentTo, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgNews = itemView.findViewById(R.id.imgNews);
            tvSentTo = itemView.findViewById(R.id.txtSendTo);
            tvTittle = itemView.findViewById(R.id.txtTittle);
            tvDate = itemView.findViewById(R.id.txtDate);

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
