package com.example.clockapp;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

public class DeviceActivity extends AppCompatActivity
{
    BluetoothDevice device;
    String userName;
    String model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Intent intent = getIntent();
        this.device = intent.getParcelableExtra("selectedDevice");
        this.userName = intent.getStringExtra("itemSystemName");
        this.model = intent.getStringExtra("modelName");
    }
}