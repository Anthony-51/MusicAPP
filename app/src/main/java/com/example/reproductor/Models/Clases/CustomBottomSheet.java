package com.example.reproductor.Models.Clases;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class CustomBottomSheet<V extends View> extends BottomSheetBehavior<V> {

    private boolean enableCollapse = true;
    public CustomBottomSheet(){}
    public CustomBottomSheet(Context context, AttributeSet attr){
        super(context,attr);
    }

    public void setEnableCollapse(boolean enableCollapse){
        this.enableCollapse = enableCollapse;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
        if(!enableCollapse){
            return false;
        }
            return super.onInterceptTouchEvent(parent, child, event);
    }

}
