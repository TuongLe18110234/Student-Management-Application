package com.example.unionline.DAO;

import com.example.unionline.DTO.News;
import com.example.unionline.DTO.Notification;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewsDAO {
    String path;
    DatabaseReference mDataBase;

    private static NewsDAO instance;

    public static NewsDAO getInstance() {
        if (instance == null) {
            instance = new NewsDAO();
        }
        return instance;
    }

    public static void setInstance(NewsDAO instance) {
        NewsDAO.instance = instance;
    }

    public NewsDAO() { path = "News"; }

    public void insertNews(News news) {
        mDataBase = FirebaseDatabase.getInstance().getReference(path);
        String key = mDataBase.push().getKey();
        news.setId(key);
        mDataBase.child(key).setValue(news);
    }
    public void setValude(News news)
    {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("News").child(news.getId()).setValue(news);
    }
    public boolean deleteNews(String id) {
        try {
            mDataBase = FirebaseDatabase.getInstance().getReference();
            mDataBase.child(path).child(id).removeValue();
            return true;
        } catch (Error error){
            return false;
        }
    }
}
