package com.example.clockapp;
import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DeviceActivity extends AppCompatActivity
{
    BluetoothDevice device;
    String userName;
    String model;
    String loginPassword;
    String wifiPassword;
    String timeDate;
    String timeTime;
    String alarmDate;
    String alarmTime;

    TextInputLayout layoutTimeDate;
    TextInputEditText textTimeDate;
    TextInputLayout layoutTimeTime;
    TextInputEditText textTimeTime;

    TextInputLayout layoutAlarmDate;
    TextInputEditText textAlarmDate;
    TextInputLayout layoutAlarmTime;
    TextInputEditText textAlarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Intent intent = getIntent();
        this.device = intent.getParcelableExtra("selectedDevice");
        this.userName = intent.getStringExtra("itemSystemName");
        this.model = intent.getStringExtra("modelName");


        this.layoutTimeDate = findViewById(R.id.datePicker);
        this.textTimeDate = findViewById(R.id.datePicker2);
        this.layoutTimeTime = findViewById(R.id.timePicker);
        this.textTimeTime = findViewById(R.id.timePicker2);

        this.layoutAlarmDate = findViewById(R.id.alarmDatePicker);
        this.textAlarmDate = findViewById(R.id.alarmDatePicker2);
        this.layoutAlarmTime = findViewById(R.id.alarmTimePicker);
        this.textAlarmTime = findViewById(R.id.alarmTimePicker2);

        this.setDataPickers(this.textTimeDate, this.textTimeTime);
        this.setDataPickers(this.textAlarmDate, this.textAlarmTime);

        findViewById(R.id.extended_fab).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                sendData();
            }
        });
    }

    void setDataPickers(TextInputEditText dataText, TextInputEditText timeText)
    {
        dataText.setKeyListener(null);
        timeText.setKeyListener(null);
        dataText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker().build();
                materialDatePicker.addOnPositiveButtonClickListener(selection ->
                {
                    Long x = selection;
                    Date date = new Date(x);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(date);
                    dataText.setText(formattedDate);
                });
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
            }
        });

        timeText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder().setInputMode(INPUT_MODE_CLOCK).setTimeFormat(TimeFormat.CLOCK_24H).build();
                materialTimePicker.addOnPositiveButtonClickListener(x ->
                {
                    int selectedHour = materialTimePicker.getHour();
                    int selectedMinute = materialTimePicker.getMinute();
                    String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeText.setText(formattedTime);
                });

                // Show the MaterialTimePicker
                materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER_TAG");
            }
        });
    }

    void sendData()
    {

    }
}

