package com.example.clockapp;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DevicesList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevicesList extends Fragment
{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Set<BluetoothDevice> pairedDevices;


    public DevicesList()
    {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DevicesList.
     */
    // TODO: Rename and change types and number of parameters
    public static DevicesList newInstance(String param1, String param2)
    {
        DevicesList fragment = new DevicesList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_devices_list, container, false);

        view.findViewById(R.id.extended_fab).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.updateList();
    }

    public void updateList()
    {
        BluetoothManager bluetoothManager = getSystemService(this.getContext(), BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        RecyclerView x = getActivity().findViewById(R.id.lista);
        List<Item> items = new ArrayList<Item>();
        if (bluetoothAdapter != null)
        {
            if (bluetoothAdapter.isEnabled())
            {
                if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0)
                {
                    ((TextView) getActivity().findViewById(R.id.noDevices)).setVisibility(View.INVISIBLE);
                    for (BluetoothDevice device : pairedDevices)
                    {
                        String deviceName = device.getName();
                        if(true) //deviceName.equals("HC-06")
                        {
                            String deviceHardwareAddress = device.getAddress();
                            items.add(new Item("NAZWA", "Clocker", deviceHardwareAddress));
                        }
                    }
                    x.setLayoutManager(new LinearLayoutManager(this.getView().getContext()));
                    x.setAdapter(new DevicesListViewAdapter(this.getView().getContext(), items));
                }
            }
        }
    }
}