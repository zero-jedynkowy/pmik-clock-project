package com.example.clockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DevicesListViewAdapter extends RecyclerView.Adapter<DevicesListViewHolder>
{
    Context context;
    List<Item> items;

    public DevicesListViewAdapter(Context context, List<Item> items)
    {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DevicesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new DevicesListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesListViewHolder holder, int position)
    {
        holder.name.setText("Clocker V1");
        holder.bluetoothAddress.setText("0b:21:0122:bbh");
    }

    @Override
    public int getItemCount()
    {
        return this.items.size();
    }
}
