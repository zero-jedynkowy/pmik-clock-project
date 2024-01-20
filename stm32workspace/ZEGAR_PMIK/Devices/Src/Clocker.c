/*
 * Clocker.c
 *
 *  Created on: Dec 18, 2023
 *      Author: zero-jedynkowy
 */
#include "Clocker.h"

void Clocker_Init(struct Clocker * myClocker, RTC_HandleTypeDef * rtcHandle)
{
	myClocker->maxScreen = 9;
	myClocker->currentScreen = 0;
	myClocker->screenTimeChanging = 5; //in seconds
	strcpy(myClocker->tableOfScreens[0][0], "WEATHER");
	strcpy(myClocker->tableOfScreens[0][1], "%s");
	strcpy(myClocker->tableOfScreens[1][0], "TEMPERATURE");
	strcpy(myClocker->tableOfScreens[1][1], "%.1f st. C");
	strcpy(myClocker->tableOfScreens[2][0], "FEELS LIKE");
	strcpy(myClocker->tableOfScreens[2][1], "%.1f st. C");
	strcpy(myClocker->tableOfScreens[3][0], "PRESSURE");
	strcpy(myClocker->tableOfScreens[3][1], "%.0f hPa");
	strcpy(myClocker->tableOfScreens[4][0], "HUMIDITY");
	strcpy(myClocker->tableOfScreens[4][1], "%.1f %");
	strcpy(myClocker->tableOfScreens[5][0], "WIND SPEED");
	strcpy(myClocker->tableOfScreens[5][1], "%.1f m.s");
	strcpy(myClocker->tableOfScreens[6][0], "WIND SPEED");
	strcpy(myClocker->tableOfScreens[6][1], "%.1f m.s");
	strcpy(myClocker->tableOfScreens[7][0], "SUNRISE");
	strcpy(myClocker->tableOfScreens[7][1], "%d:%d");
	strcpy(myClocker->tableOfScreens[8][0], "SUNSET");
	strcpy(myClocker->tableOfScreens[8][1], "%d:%d");
	strcpy(myClocker->tableOfScreens[9][0], "CITY");
	strcpy(myClocker->tableOfScreens[9][1], "%s");
	myClocker->sTime = (RTC_TimeTypeDef *)malloc(sizeof(RTC_TimeTypeDef));
	*myClocker->sTime = (RTC_TimeTypeDef){0};
	myClocker->sDate = (RTC_DateTypeDef *)malloc(sizeof(RTC_DateTypeDef));
	*myClocker->sDate = (RTC_DateTypeDef){0};
	myClocker->sAlarm = (RTC_AlarmTypeDef *)malloc(sizeof(RTC_DateTypeDef));
	*myClocker->sAlarm = (RTC_AlarmTypeDef){0};
	myClocker->rtcHandle = rtcHandle;
}

void Clocker_Set_Time(struct Clocker * myClocker, uint8_t newHours, uint8_t newMinutes, uint8_t newSeconds)
{
	myClocker->sTime->Hours = newHours;
	myClocker->sTime->Minutes = newMinutes;
	myClocker->sTime->Seconds = newSeconds;
	myClocker->sTime->DayLightSaving = RTC_DAYLIGHTSAVING_NONE;
	myClocker->sTime->StoreOperation = RTC_STOREOPERATION_RESET;
	HAL_RTC_SetTime(myClocker->rtcHandle, myClocker->sTime, RTC_FORMAT_BIN);
}

void Clocker_Set_Alarm(struct Clocker * myClocker, uint8_t alarmHours, uint8_t alarmMinutes)
{
	myClocker->sAlarm->AlarmTime.Hours = alarmHours;
	myClocker->sAlarm->AlarmTime.Minutes = alarmMinutes;
	myClocker->sAlarm->AlarmTime.Seconds = 0x0;
	myClocker->sAlarm->AlarmTime.SubSeconds = 0x0;
	myClocker->sAlarm->AlarmTime.DayLightSaving = RTC_DAYLIGHTSAVING_NONE;
	myClocker->sAlarm->AlarmTime.StoreOperation = RTC_STOREOPERATION_RESET;
	myClocker->sAlarm->AlarmMask = RTC_ALARMMASK_DATEWEEKDAY|RTC_ALARMMASK_SECONDS;
	myClocker->sAlarm->AlarmSubSecondMask = RTC_ALARMSUBSECONDMASK_ALL;
	myClocker->sAlarm->AlarmDateWeekDaySel = RTC_ALARMDATEWEEKDAYSEL_DATE;
	myClocker->sAlarm->AlarmDateWeekDay = 0x1;
	myClocker->sAlarm->Alarm = RTC_ALARM_A;
	HAL_RTC_SetAlarm(myClocker->rtcHandle, myClocker->sAlarm, RTC_FORMAT_BCD);
}

void Clocker_Segment_Update(struct Clocker * myClocker)
{
	uint8_t tempTime[4] = {0};
	HAL_RTC_GetTime(myClocker->rtcHandle, myClocker->sTime, RTC_FORMAT_BIN);
	HAL_RTC_GetDate(myClocker->rtcHandle, myClocker->sDate, RTC_FORMAT_BIN);
	tempTime[0] = Clocker_Convert_Int_to_Segment((myClocker->sTime->Hours)/10);
	tempTime[1] = Clocker_Convert_Int_to_Segment((myClocker->sTime->Hours)%10);
	tempTime[2] = Clocker_Convert_Int_to_Segment((myClocker->sTime->Minutes)/10);
	tempTime[3] = Clocker_Convert_Int_to_Segment((myClocker->sTime->Minutes)%10);
	tm1637_DisplayHandle(7, tempTime);
}

uint8_t Clocker_Convert_Int_to_Segment(uint8_t c)
{
	switch(c)
	{
		case 0 : return 0x3f;
		case 1 : return 0x06;
		case 2 : return 0x5b;
		case 3 : return 0x4f;
		case 4 : return 0x66;
		case 5 : return 0x6d;
		case 6 : return 0x7d;
		case 7 : return 0x07;
		case 8 : return 0x7f;
		case 9 : return 0x6f;
	}
	return 0x3f;
}
