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
import android.os.AsyncTask;
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

    public String createData()
    {
        String a = "WIFI";
        String b = "DATE";
        String c = "ALAR";
        String d = "WEAT";

        boolean wifi = this.listOfSwitches.get("wifiSwitcher").isChecked();
        boolean dateTime = this.listOfSwitches.get("dateTimeSwitcher").isChecked();
        boolean alarm = this.listOfSwitches.get("dateTimeSwitcher").isChecked();
        boolean weather = this.listOfSwitches.get("weatherSwitcher").isChecked();

        if(wifi)
        {
            a += "__ONN__";
            a += ((TextInputEditText)findViewById(R.id.wifiSSID2)).getText().toString();
            a += "__";
            a += ((TextInputEditText)findViewById(R.id.wifiPassword2)).getText().toString();
        }
        else
        {
            a += "__OFF__";
        }
        if(wifi && dateTime)
        {
            b += "__ONN__";
            b += "TIME";
            b += "__ONN__";
        }
        else
        {
            b += "__OFF__";
            String temp = ((TextInputEditText)findViewById(R.id.datePicker2)).getText().toString();
            String x[] = temp.split("/");
            for(int i=0; i<x.length; i++)
            {
                b += x[i];
                b += "__";
            }
            b += "TIME";
            b += "__OFF__";
            temp = ((TextInputEditText)findViewById(R.id.timePicker2)).getText().toString();
            x = temp.split(":");
            for(int i=0; i<x.length; i++)
            {
                b += x[i];
                b += "__";
            }
        }
        if(alarm)
        {
            c += "__ONN__";
            String temp = ((TextInputEditText)findViewById(R.id.alarmDatePicker2)).getText().toString();
            String x[] = temp.split("/");
            for(int i=0; i<x.length; i++)
            {
                c += x[i];
                c += "__";
            }
            temp = ((TextInputEditText)findViewById(R.id.alarmTimePicker2)).getText().toString();
            x = temp.split(":");
            for(int i=0; i<x.length; i++)
            {
                b += x[i];
                b += "__";
            }
        }
        else
        {
            c += "__OFF__";
        }
        if(weather && wifi)
        {
            d += "__ON__";
            d += ((TextInputEditText)findViewById(R.id.weatherCityPicker2)).getText().toString();
        }
        else
        {
            d += "__OFF__";
        }
        a += generatePlaceholder(50 - a.length(), "_");
        b += generatePlaceholder(50 - b.length(), "_");
        c += generatePlaceholder(50 - c.length(), "_");
        d += generatePlaceholder(50 - d.length(), "_");
        return  a + "\n" + b + "\n" + c + "\n" + d + "\n";
    }

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
                String temp = DeviceActivity.this.createData();
                System.out.println(temp.length());
                BluetoothThread y = new BluetoothThread(DeviceActivity.this.device, temp);
                y.start();




//                if(!sendData("abcdxx\r\n"))
//                {
//                    MaterialAlertDialogBuilder x = new MaterialAlertDialogBuilder(DeviceActivity.this);
//                    x.setTitle("Wysłanie danych nie powiodło się!");
//                    x.setMessage("Sprawdz czy urządzenie jest w zasięgu telefonu.");
//                    x.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            dialog.dismiss();
//                        }
//                    });;
//                    x.show();
//                }
//                else
//                {
//                    MaterialAlertDialogBuilder x = new MaterialAlertDialogBuilder(DeviceActivity.this);
//                    x.setMessage("Przesłano dane do urządzenia!");
//                    x.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which)
//                        {
//                            dialog.dismiss();
//                        }
//                    });;
//                    x.show();
//                }
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



    String generatePlaceholder(int len, String holder)
    {
        String x = "";
        for(int i=0; i<len; i++)
        {
            x += holder;
        }
        return x;
    }

    class BluetoothThread extends Thread
    {
        private BluetoothDevice device;
        private String name;

        public BluetoothThread(BluetoothDevice device, String name)
        {
            super();
            this.device = device;
            this.name = name;
        }

        @Override
        public void run()
        {
            super.run();
            boolean z = false;
            try
            {
                z = this.sendData(this.name);
            }
            catch(Exception y)
            {
                z = false;
            }
            if(!z)
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
                    });
                    runOnUiThread(()->{
                        ((LinearProgressIndicator)(findViewById(R.id.loading))).setIndeterminate(false);
                        x.show();
                    });
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
                runOnUiThread(()->{
                    ((LinearProgressIndicator)(findViewById(R.id.loading))).setIndeterminate(false);
                    x.show();
                });
            }
        }


        boolean sendData(String dataToSend)
        {
            ((LinearProgressIndicator)(findViewById(R.id.loading))).setIndeterminate(true);
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            BluetoothSocket bluetoothSocket = null;
            if (ActivityCompat.checkSelfPermission(DeviceActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
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

