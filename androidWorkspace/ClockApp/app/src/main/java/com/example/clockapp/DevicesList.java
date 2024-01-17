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

interface OnItemClickListener
{
    void onItemClick(int position);
}

public class DevicesList extends Fragment implements OnItemClickListener
{
    Set<BluetoothDevice> pairedDevices;
    List<BluetoothDevice> listPairedDevices;
    List<Item> items;

    public static DevicesList newInstance(String param1, String param2)
    {
        DevicesList fragment = new DevicesList();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        this.items = new ArrayList<Item>();
        this.listPairedDevices = new ArrayList<BluetoothDevice>();
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
                        if(deviceName.equals("HC-06")) //deviceName.equals("HC-06")
                        {
                            String deviceHardwareAddress = device.getAddress();
                            items.add(new Item("NAZWA", "Clocker", deviceHardwareAddress));
                            this.listPairedDevices.add(device);
                        }
                    }
                    x.setLayoutManager(new LinearLayoutManager(this.getView().getContext()));
                    DevicesListViewAdapter y = new DevicesListViewAdapter(this.getView().getContext(), items);
                    y.setOnItemClickListener(this);
                    x.setAdapter(y);
                }
            }
        }
    }

    @Override
    public void onItemClick(int position)
    {
        Intent myIntent= new Intent(this.getContext(), DeviceActivity.class);
        myIntent.putExtra("selectedDevice", this.listPairedDevices.get(position));
        myIntent.putExtra("itemSystemName", items.get(position).getUserName());
        myIntent.putExtra("modelName", items.get(position).getModelName());
        startActivity(myIntent);
    }
}