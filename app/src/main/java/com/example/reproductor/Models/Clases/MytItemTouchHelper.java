package com.example.reproductor.Models.Clases;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reproductor.Interfaces.ItemTouchHelperListener;
import com.example.reproductor.R;

public class MytItemTouchHelper extends ItemTouchHelper.Callback {
    private final ItemTouchHelperListener mListener;

    public MytItemTouchHelper(ItemTouchHelperListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        ColorDrawable color = (ColorDrawable) viewHolder.itemView.getBackground();
        int col = color.getColor();
        if(viewHolder.itemView.getContext().getResources().getColor(R.color.light_gray) == col){
            viewHolder.itemView.setBackgroundTintList(ContextCompat.getColorStateList(viewHolder.itemView.getContext(), R.color.light_gray));
        }else{
            viewHolder.itemView.setBackgroundTintList(ContextCompat.getColorStateList(viewHolder.itemView.getContext(), R.color.black));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            viewHolder.itemView.setBackgroundTintList(ContextCompat.getColorStateList(viewHolder.itemView.getContext(),R.color.light_gray));
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mListener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onItemSwiped(viewHolder.getAdapterPosition());
    }
}
