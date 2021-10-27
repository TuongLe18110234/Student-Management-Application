package com.example.unionline.Adapters.Admin;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ItemTouchHelperListener adminListener;

    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, ItemTouchHelperListener adminListener) {
        super(dragDirs, swipeDirs);
        this.adminListener = adminListener;
    }

    @Override
    public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (adminListener != null) {
            adminListener.onSwipeListener(viewHolder);
        }
    }

    @Override
    public void onSelectedChanged(@Nullable @org.jetbrains.annotations.Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView = ((AccountAdapter.AccountViewHolder) viewHolder).layoutForegroundQlk;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((AccountAdapter.AccountViewHolder) viewHolder).layoutForegroundQlk;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((AccountAdapter.AccountViewHolder) viewHolder).layoutForegroundQlk;
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((AccountAdapter.AccountViewHolder) viewHolder).layoutForegroundQlk;
        getDefaultUIUtil().clearView(foregroundView);
    }
}
