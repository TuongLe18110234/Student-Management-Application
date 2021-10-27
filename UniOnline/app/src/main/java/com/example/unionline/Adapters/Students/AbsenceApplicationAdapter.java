package com.example.unionline.Adapters.Students;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unionline.Common;
import com.example.unionline.DTO.AbsenceApplication;
import com.example.unionline.DTO.Enrollment;
import com.example.unionline.R;

import java.util.ArrayList;

public class AbsenceApplicationAdapter extends RecyclerView.Adapter<AbsenceApplicationAdapter.ViewHolder> {

    Context context;
    ArrayList<AbsenceApplication> absenceApplications;
    RecyclerViewClickListener listener;

    public AbsenceApplicationAdapter(Context context, ArrayList<AbsenceApplication> absenceApplications, RecyclerViewClickListener listener) {
        this.context = context;
        this.absenceApplications = absenceApplications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_absence_application, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Map data with view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AbsenceApplication absenceApplication = absenceApplications.get(position);

        holder.tvCreateDate.setText("Ngày gửi: " + absenceApplication.getDateCreate());
        holder.tvReasonAbsent.setText(absenceApplication.getReason());
        holder.tvClassName.setText(absenceApplication.getClassName());
        holder.tvClassTime.setText(absenceApplication.getClassTime());
        holder.tvAbsentState.setText(Common.aaNames.get(absenceApplication.getState()));
        holder.tvDateOff.setText(absenceApplication.getDateOff());
    }

    @Override
    public int getItemCount() {
        return absenceApplications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        TextView tvCreateDate, tvReasonAbsent, tvClassName, tvClassTime, tvAbsentState, tvDateOff;

        // Set view
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout itemAA;

            tvCreateDate = itemView.findViewById(R.id.tvCreateDate);
            tvReasonAbsent = itemView.findViewById(R.id.tvReasonAbsent);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassTime = itemView.findViewById(R.id.tvClassTime);
            tvAbsentState = itemView.findViewById(R.id.tvAbsentState);
            tvDateOff = itemView.findViewById(R.id.tvDateOff);

            itemView.setOnClickListener(this);

            itemAA = itemView.findViewById(R.id.itemAbcenceApp);
            itemAA.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCLick(itemView, getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            listener.onCreateContextMenu(menu, getAdapterPosition());
        }
    }

    // Set on click and on create context menu
    public interface RecyclerViewClickListener {
        void onCLick(View v, int position);
        void onCreateContextMenu(ContextMenu menu, int position);
    }
}
