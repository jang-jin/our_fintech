package com.example.david0926.templete;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecycleHolder_People extends RecyclerView.ViewHolder{

    public TextView name, msg;
    public ImageView profile;
    public int profileId;

    public RecycleHolder_People(View itemView, int imageId) {
        super(itemView);
        name = itemView.findViewById(R.id.people_name);
        msg = itemView.findViewById(R.id.people_msg);
        profile = itemView.findViewById(R.id.people_profile);
        profileId = imageId;


    }
}
