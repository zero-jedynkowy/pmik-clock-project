#  pmik-clock-project

## Description

A simple unfinished and abandoned project for a weather clock station with an Android app.

I'm a co-maintener of this project with Jakub Jastrzębski.

![](readme-files/pmik-clock-app-example.gif)

## Project's assumptions:

* A device with a timer/clock with an alarm on the LCD screen;
* The device is based on the STM32 MCU and also with ESP8266 Wi-fi module;
* The Clock can download data about acctual weather and time from the internet by https request api;
* Settings of the device can change in the android app (Wi-fi password, city to the weather etc);
* Settings are sent by bluetooth to the STM32 MCU in JSON format, processed in ESP8266;
* ESP8266 is responsible to get informations about the weather forecast, process it and return to the STM32 MCU;
* STM32 MCU is responsible to show the time, set an alarm, process bluetooth settings data, show everything on the LCD screen.


## Languages
* STM32 - C
* ESP8266 - Lua (NodeMCU framework)
* Android App - Java