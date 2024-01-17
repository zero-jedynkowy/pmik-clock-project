package com.example.clockapp;

import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
    Map<String, MaterialSwitch> listOfSwitches;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Intent intent = getIntent();
        this.device = intent.getParcelableExtra("selectedDevice");
        this.userName = intent.getStringExtra("itemSystemName");
        this.model = intent.getStringExtra("modelName");

        ((TextView)findViewById(R.id.deviceTitle)).setText(this.userName);
        listOfSwitches = new HashMap<>();
        listOfSwitches.put("wifiSwitcher", (MaterialSwitch) findViewById(R.id.wifiSwitcher));
        listOfSwitches.put("dateTimeSwitcher", (MaterialSwitch) findViewById(R.id.dataTimeSwitcher));
        listOfSwitches.put("alarmSwitcher", (MaterialSwitch) findViewById(R.id.alarmSwitcher));
        listOfSwitches.put("weatherSwitcher", (MaterialSwitch) findViewById(R.id.weatherSwitcher));

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
                ((LinearProgressIndicator)(findViewById(R.id.loading))).setIndeterminate(true);
                if(!sendData("abcdxx\r\n"))
                {
                    MaterialAlertDialogBuilder x = new MaterialAlertDialogBuilder(DeviceActivity.this);
                    x.setTitle("Wysłanie danych nie powiodło się!");
                    x.setMessage("Sprawdz czy urządzenie jest w zasięgu telefonu.");
                    x.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });;
                    x.show();
                }
                else
                {
                    MaterialAlertDialogBuilder x = new MaterialAlertDialogBuilder(DeviceActivity.this);
                    x.setMessage("Przesłano dane do urządzenia!");
                    x.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });;
                    x.show();
                }
            }

        });
        this.setSwitchers();
    }

    void setSwitchers()
    {
        this.listOfSwitches.get("wifiSwitcher").setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            if (isChecked)
            {
                ((TextInputLayout)findViewById(R.id.wifiSSID)).setEnabled(true);
                ((TextInputLayout)findViewById(R.id.wifiPassword)).setEnabled(true);
                this.listOfSwitches.get("dateTimeSwitcher").setEnabled(true);
                this.listOfSwitches.get("weatherSwitcher").setEnabled(true);
            }
            else
            {
                ((TextInputLayout)findViewById(R.id.wifiSSID)).setEnabled(false);
                ((TextInputLayout)findViewById(R.id.wifiPassword)).setEnabled(false);
                this.listOfSwitches.get("dateTimeSwitcher").setEnabled(false);
                this.listOfSwitches.get("weatherSwitcher").setEnabled(false);
            }
        });

        this.listOfSwitches.get("dateTimeSwitcher").setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            if (isChecked)
            {
                ((TextInputLayout)findViewById(R.id.datePicker)).setEnabled(false);
                ((TextInputLayout)findViewById(R.id.timePicker)).setEnabled(false);
            }
            else
            {
                ((TextInputLayout)findViewById(R.id.datePicker)).setEnabled(true);
                ((TextInputLayout)findViewById(R.id.timePicker)).setEnabled(true);
            }
        });

        this.listOfSwitches.get("alarmSwitcher").setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            if (isChecked)
            {
                ((TextInputLayout)findViewById(R.id.alarmDatePicker)).setEnabled(true);
                ((TextInputLayout)findViewById(R.id.alarmTimePicker)).setEnabled(true);
            }
            else
            {
                ((TextInputLayout)findViewById(R.id.alarmDatePicker)).setEnabled(false);
                ((TextInputLayout)findViewById(R.id.alarmTimePicker)).setEnabled(false);
            }
        });

        this.listOfSwitches.get("weatherSwitcher").setOnCheckedChangeListener((buttonView, isChecked) ->
        {

            if (isChecked)
            {
                ((TextInputLayout)findViewById(R.id.weatherCityPicker)).setEnabled(true);
            }
            else
            {
                ((TextInputLayout)findViewById(R.id.weatherCityPicker)).setEnabled(false);
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

                materialTimePicker.show(getSupportFragmentManager(), "TIME_PICKER_TAG");
            }
        });
    }

    boolean sendData(String dataToSend)
    {
        ((LinearProgressIndicator)(findViewById(R.id.loading))).setIndeterminate(true);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothSocket bluetoothSocket = null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }
        try
        {
            bluetoothSocket = this.device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
        }
        catch (Exception x)
        {
            try
            {
                bluetoothSocket.close();
            }
            catch (IOException e)
            {
                return false;
            }
            return false;
        }


        if (bluetoothSocket != null)
        {
            try
            {
                OutputStream outputStream = bluetoothSocket.getOutputStream();
                byte[] bytes = dataToSend.getBytes();
                outputStream.write(bytes);
                bluetoothSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else
        {
            return false;
        }
        return true;
    }
}

