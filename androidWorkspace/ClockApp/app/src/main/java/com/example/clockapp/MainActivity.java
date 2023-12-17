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
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Kwestia Bluetooth")
                    .setMessage("Aplikacja potrzebuje Bluetooth do poprawnego działania!")
                    .setNeutralButton("OK", (a,b) -> {requestPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT);}).show();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.item_1)
        {
            this.changeFragments(this.devicesListFragment);
            return true;
        }
        else
        {
            this.changeFragments(this.settingsListFragment);
            return true;
        }
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
                        new MaterialAlertDialogBuilder(this)
                                .setTitle("Kwestia Bluetooth")
                                .setMessage("Nie wykryto Bluetooth. Aplikacja potrzebuje Bluetooth do poprawnego działania!")
                                .setNeutralButton("WYJŚCIE", (a,b) -> {this.finishAffinity();}).show();

                    }
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                else
                {
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Kwestia Bluetooth")
                            .setMessage("Wykryto brak dostępu do Bluetooth. Aplikacja potrzebuje Bluetooth do poprawnego działania! Nadaj jej uprawnienia w ustawieniach systemowych!")
                            .setNeutralButton("WYJŚCIE", (a,b) -> {System.exit(0);}).show();
                }
    });

    private void getPermission(String permission)
    {
        if (ContextCompat.checkSelfPermission(this.getBaseContext(), permission) == PackageManager.PERMISSION_GRANTED)
        {
            // You can use the API that requires the permission.
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
        {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.

        }
        else
        {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(permission);
        }
    }
}