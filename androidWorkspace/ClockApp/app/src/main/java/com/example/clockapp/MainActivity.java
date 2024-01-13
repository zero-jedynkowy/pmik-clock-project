package com.example.clockapp;

import static android.app.ProgressDialog.show;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.clockapp.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener
{
    private static final int REQUEST_ENABLE_BT = 1;
    private NavigationBarView bottomMenu;
    private ActivityMainBinding binding;
    private DevicesList devicesListFragment;
    private Settings settingsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.devicesListFragment = new DevicesList();
        this.settingsListFragment = new Settings();
        this.changeFragments(this.devicesListFragment);
        this.bottomMenu = findViewById(R.id.bottom_navigation);
        this.binding.bottomNavigation.setOnItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
        {
            requestPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT);
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH_CONNECT))
        {
            requestPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT);
        }
        else
        {
            String a = "Witaj!";
            String b = "Aplikacja potrzebuje Bluetooth do poprawnego działania!";
            String c = "OK";
            this.createSimpleDialog(a, b).setNeutralButton(c, (x, y) -> {requestPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT);}).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.item_1)
        {
            this.changeFragments(this.devicesListFragment);
        }
        else
        {
            this.changeFragments(this.settingsListFragment);
        }
        return true;
    }

    private MaterialAlertDialogBuilder createSimpleDialog(String dialogTitle, String dialogMessage)
    {
        MaterialAlertDialogBuilder tempDialog = new MaterialAlertDialogBuilder(this);
        tempDialog.setTitle(dialogTitle);
        tempDialog.setMessage(dialogMessage);
        return  tempDialog;
    }

    private void changeFragments(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContent, fragment);
        fragmentTransaction.commit();
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->
    {
        if (isGranted)
        {
            BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null)
            {
                String a = "Brak Bluetooth";
                String b = "Nie wykryto Bluetooth na urządzeniu. Aplikacja potrzebuje Bluetooth do poprawnego działania!";
                String c = "WYJŚCIE";
                this.createSimpleDialog(a, b).setNeutralButton(c, (x, y) ->{System.exit(0);}).show();
            }
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            this.devicesListFragment.updateList();
        }
        else
        {
            String a = "Dostep do Bluetooth";
            String b = "Wykryto brak dostępu do Bluetooth. Aplikacja potrzebuje Bluetooth do poprawnego działania! Nadaj jej uprawnienia w ustawieniach systemowych.";
            String c = "WYJŚCIE";
            this.createSimpleDialog(a, b).setNeutralButton(c, (x, y) -> {System.exit(0);}).show();
        }

    });

    @Override
    protected void onResume()
    {
        super.onResume();
        this.devicesListFragment.updateList();
    }
}