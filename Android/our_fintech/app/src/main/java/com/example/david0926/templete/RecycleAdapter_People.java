package com.example.david0926.templete;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter_People extends RecyclerView.Adapter<RecycleHolder_People> {

    List<RecycleModel_People> items = new ArrayList<>();

    public void add(RecycleModel_People data){
        items.add(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleHolder_People onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item_people, parent, false);
        return new RecycleHolder_People(v, R.drawable.ic_person);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleHolder_People holder, int position) {
        RecycleModel_People item = items.get(position);
        holder.name.setText(item.getName());
        holder.msg.setText(item.getMsg());
        holder.profile.setImageResource(items.get(position).getProfileId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
