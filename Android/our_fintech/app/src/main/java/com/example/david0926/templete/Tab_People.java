package com.example.david0926.templete;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Tab_People extends Fragment{


    RecyclerView rcv;
    RecycleAdapter_People rcvAdap;

//    public void showPopup(View v) {
//        PopupMenu popup = new PopupMenu(getContext(), v);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.people_more_drawer, popup.getMenu());
//        popup.show();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_people, container, false);
        rcv = v.findViewById(R.id.people_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(lm);
        rcvAdap = new RecycleAdapter_People();
        rcv.setAdapter(rcvAdap);


//        View itemImageView = inflater.inflate(R.layout.recycle_item_people, container, false);
//        ImageView profile = itemImageView.findViewById(R.id.people_profile);
//        profile.setBackground(new ShapeDrawable(new OvalShape()));
//        profile.setClipToOutline(true);
        RecycleModel_People model = new RecycleModel_People();
        model.setProfileId(R.drawable.omnyomnyom_profile);
        model.setName("Hatban");
        model.setMsg("#"+"Coding_Slave");
        rcvAdap.add(model);
        rcvAdap.notifyDataSetChanged();

        for(int i=0;i<10;i++) {
//            itemImageView = inflater.inflate(R.layout.recycle_item_people, container, false);
//            profile = itemImageView.findViewById(R.id.people_profile);
//            profile.setBackground(new ShapeDrawable(new OvalShape()));
//            profile.setClipToOutline(true);
            model = new RecycleModel_People();
            model.setProfileId(R.drawable.ic_person);
            model.setName("Someone");
            model.setMsg("#"+"Coding All Day");
            rcvAdap.add(model);
            rcvAdap.notifyDataSetChanged();
        }




        return v;
    }



}
