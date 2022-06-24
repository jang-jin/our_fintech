package com.example.david0926.templete;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class Tab_Chat extends Fragment{

    RecyclerView rcv;
    RecycleAdapter_Chat rcvAdap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_chat, container, false);
        rcv = v.findViewById(R.id.chat_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(lm);
        rcvAdap = new RecycleAdapter_Chat();
        rcv.setAdapter(rcvAdap);



        //recyclerview
//        View itemImageView = inflater.inflate(R.layout.recycle_item_chat, container, false);
//        ImageView profile = itemImageView.findViewById(R.id.chat_profile);
//        profile.setBackground(new ShapeDrawable(new OvalShape()));
//        profile.setClipToOutline(true);
        RecycleModel_Chat model = new RecycleModel_Chat();
        model.setProfileId(R.drawable.omnyomnyom_profile);
        model.setName("Hatban");
        model.setMsg("Have a nice day!");
        model.setTime("3:25 PM");
        rcvAdap.add(model);
        rcvAdap.notifyDataSetChanged();

        for(int i=0;i<10;i++) {
//            itemImageView = inflater.inflate(R.layout.recycle_item_chat, container, false);
//            profile = itemImageView.findViewById(R.id.chat_profile);
//            profile.setBackground(new ShapeDrawable(new OvalShape()));
//            profile.setClipToOutline(true);
            model = new RecycleModel_Chat();
            model.setProfileId(R.drawable.ic_person);
            model.setName("Someone");
            model.setMsg("Hello World!");
            model.setTime("12:53 PM");
            rcvAdap.add(model);
            rcvAdap.notifyDataSetChanged();
        }

        rcv.addOnItemTouchListener(new RecycleListener(getContext(), rcv, new RecycleListener.OnClickListener() {
            @Override
            public void ItemClick(View v, int position) {
                Toast.makeText(getContext(), position + "clicked", Toast.LENGTH_SHORT).show();
            }
        }));
        //end of recyclerview


        return v;


    }





}
