package com.example.clockapp;

public class Item
{
    private String name;
    private String bluetoothAddress;

    public Item(String name, String bluetoothAddress)
    {
        this.name = name;
        this.bluetoothAddress = bluetoothAddress;
    }

    public Item()
    {
        this.name = "HC06";
        this.bluetoothAddress = "B0:123:SDA:123";
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBluetoothAddress()
    {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress)
    {
        this.bluetoothAddress = bluetoothAddress;
    }
}
