package com.example.david0926.templete;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecycleListener implements RecyclerView.OnItemTouchListener{

    public interface OnClickListener{
        void ItemClick(View v, int position);
    }
    private OnClickListener onClickListener;

    GestureDetector gestureDetector;

    public RecycleListener(Context context, final RecyclerView recyclerView, OnClickListener listener){
         onClickListener = listener;
         gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
             @Override
             public boolean onSingleTapUp(MotionEvent e) {
                 View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                 if(child!=null && onClickListener!=null){
                     onClickListener.ItemClick(child, recyclerView.getChildAdapterPosition(child));
                 }
                 return true;
             }
         });

    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if(child!=null && onClickListener!=null && gestureDetector.onTouchEvent(e)){
            int currentPosition = rv.getChildAdapterPosition(child);
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


}
