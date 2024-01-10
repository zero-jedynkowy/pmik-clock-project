package com.example.clockapp;

public class Item
{
    private String userName;
    private String modelName;
    private String bluetoothAddress;

    public Item(String userName, String modelName, String bluetoothAddress)
    {
        this.userName = userName;
        this.modelName = modelName;
        this.bluetoothAddress = bluetoothAddress;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getModelName()
    {
        return modelName;
    }

    public void setModelName(String modelName)
    {
        this.modelName = modelName;
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
