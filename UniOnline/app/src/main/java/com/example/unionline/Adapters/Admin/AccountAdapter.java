package com.example.unionline.Adapters.Admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.DTO.User;
import com.example.unionline.R;
import com.example.unionline.Views.Admin.AdminParentAccountActivity;
import com.example.unionline.Views.Admin.AdminQlkAccountActivity;
import com.example.unionline.Views.Admin.AdminStudentAccountActivity;
import com.example.unionline.Views.Admin.AdminTeacherAccountActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private ArrayList<User> userList;

    public AccountAdapter(AdminQlkAccountActivity adminQlkAccountActivity, ArrayList<User> userList) {
        this.userList = userList;
    }

    public AccountAdapter(AdminParentAccountActivity adminParentAccountActivity, ArrayList<User> userList) {
        this.userList = userList;
    }

    public AccountAdapter(AdminStudentAccountActivity adminStudentAccountActivity, ArrayList<User> userList) {
        this.userList = userList;
    }

    public AccountAdapter(AdminTeacherAccountActivity adminTeacherAccountActivity, ArrayList<User> userList) {
        this.userList = userList;
    }

    public static void filterList(ArrayList<User> userList) {
    }

    @NonNull
    @NotNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_manage_qlkhoaaccount, parent, false);

        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AccountViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) {
            return;
        }
        holder.tvQlkName.setText(user.getName());
        holder.tvQlkEmail.setText(user.getEmail());
    }



    @Override
    public int getItemCount() {
        if (userList != null)
            return userList.size();
        return 0;
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        TextView tvQlkName, tvQlkEmail;
        ImageView btnQlkEdit;
        RelativeLayout layoutForegroundQlk;

        public AccountViewHolder(@NonNull @NotNull View itemView) {

            super(itemView);

            layoutForegroundQlk = itemView.findViewById(R.id.layout_foreground_qlk);
            tvQlkName = itemView.findViewById(R.id.tv_admin_qlk_name);
            tvQlkEmail = itemView.findViewById(R.id.tv_admin_qlk_email);
            btnQlkEdit = itemView.findViewById(R.id.btn_edit_qlk);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });
        }

    }
    public interface OnItemClickListener{
        void onClick(View v, int position);


    }
    public void removeItem (int index, String usernameDelete) {
        Query query = FirebaseDatabase.getInstance().getReference("User/Department Manager").orderByChild("name").equalTo(usernameDelete);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot qlkSnapshot: dataSnapshot.getChildren()) {
                    qlkSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Cancel", "onCancelled", databaseError.toException());
            }
        });
        userList.remove(index);
        notifyItemRemoved(index);
    }

    public void undoItem (User user, int index) {
        userList.add(index, user);

        notifyItemInserted(index);
    }



}
