package com.example.clockapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.Map;

public class DevicesListViewHolder extends RecyclerView.ViewHolder
{
    public MaterialCardView cardView;
    public TextView userName;
    public TextView modelName;
    public TextView bluetoothAddress;
    public DevicesListViewHolder(@NonNull View itemView)
    {
        super(itemView);
        this.cardView = itemView.findViewById(R.id.card);
        this.userName = itemView.findViewById(R.id.name);
        this.modelName = itemView.findViewById(R.id.model);
        this.bluetoothAddress = itemView.findViewById(R.id.address);
    }
}
