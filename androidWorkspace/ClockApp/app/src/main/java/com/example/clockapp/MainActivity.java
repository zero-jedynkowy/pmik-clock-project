package com.example.clockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.clockapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener
{
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
}