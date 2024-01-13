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
    private OnItemClickListener clickListener;
    public DevicesListViewAdapter(Context context, List<Item> items)
    {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DevicesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new DevicesListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false), clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DevicesListViewHolder holder, int position)
    {
        holder.userName.setText(String.format(holder.userName.getText().toString(), items.get(position).getUserName()));
        holder.modelName.setText(String.format(holder.modelName.getText().toString(), items.get(position).getModelName()));
        holder.bluetoothAddress.setText(String.format(holder.bluetoothAddress.getText().toString(), items.get(position).getBluetoothAddress()));
    }

    @Override
    public int getItemCount()
    {
        return this.items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

}
