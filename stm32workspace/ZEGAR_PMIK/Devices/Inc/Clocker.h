/*
 * Clocker.h
 *
 *  Created on: Dec 18, 2023
 *      Author: zero-jedynkowy
 */

#ifndef __CLOCKER_H
#define __CLOCKER_H

#include <stdint.h>
#include "TM1637.h"
#include "LCD1602.h"
#include "stm32l0xx_hal_rtc.h"
#include <stdio.h>
#include <stdlib.h>

struct
{
	uint8_t maxScreen;
	uint8_t currentScreen;
	uint8_t screenTimeChanging; //in seconds
	char tableOfScreens[10][2][16];
	RTC_TimeTypeDef * sTime;
	RTC_DateTypeDef * sDate;
	RTC_AlarmTypeDef * sAlarm;
	RTC_HandleTypeDef * rtcHandle;
} typedef Clocker;

void Clocker_Init(Clocker * myClocker, RTC_HandleTypeDef * rtcHandle, TIM_HandleTypeDef * timSegment, TIM_HandleTypeDef * timScreen);
void Clocker_Set_Time(Clocker * myClocker, uint8_t newHours, uint8_t newMinutes, uint8_t newSeconds);
void Clocker_Set_Alarm(Clocker * myClocker, uint8_t alarmHours, uint8_t alarmMinutes);
uint8_t Clocker_Convert_Int_to_Segment(uint8_t c);
void Clocker_Segment_Update(Clocker * myClocker);
void Clocker_Change_Screen(Clocker * myClocker);
void Clocker_Change_Time(Clocker * myClocker, HAL_TIM_StateTypeDef * myTimer);

#endif
