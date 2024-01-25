/**
 * @file Clocker.c
 * @brief Plik żródłowy bilioteki Clocker; zawiera definicje funkcji
 *
 * Detailed description of the file goes here.
 *
 * @author Karol Ambroziński, Jakub Jastrzębski
 * @date   January 25, 2024
 */


#include "Clocker.h"

void Clocker_Init(Clocker * myClocker, RTC_HandleTypeDef * rtcHandle, TIM_HandleTypeDef * timSegment, TIM_HandleTypeDef * timScreen)
{
	DF_Init(20);
	HAL_GPIO_WritePin(SCLK_GPIO_Port, SCLK_Pin, GPIO_PIN_SET);
	HAL_GPIO_WritePin(SDO_GPIO_Port, SDO_Pin, GPIO_PIN_SET);
	myClocker->maxScreen = 9;
	myClocker->currentScreen = 0;
	myClocker->screenTimeChanging = 5; //in seconds
	Clocker_Set_Screens(myClocker);
	myClocker->sTime = (RTC_TimeTypeDef *)malloc(sizeof(RTC_TimeTypeDef));
	*myClocker->sTime = (RTC_TimeTypeDef){0};
	myClocker->sDate = (RTC_DateTypeDef *)malloc(sizeof(RTC_DateTypeDef));
	*myClocker->sDate = (RTC_DateTypeDef){0};
	myClocker->sAlarm = (RTC_AlarmTypeDef *)malloc(sizeof(RTC_DateTypeDef));
	*myClocker->sAlarm = (RTC_AlarmTypeDef){0};
	myClocker->rtcHandle = rtcHandle;
	myClocker->alarmTimer = 0;
	myClocker->screenTimer = 0;
	myClocker->alarm = 0;
	uint8_t flags[4] = {0, 0, 0, 0};
	HAL_TIM_Base_Start_IT(timSegment);
	HAL_TIM_Base_Start(timScreen);
	lcd_init();
	lcd_clear();

}

void Clocker_Set_Screens(Clocker * myClocker)
{
	strcpy(myClocker->tableOfScreens[0][0], "WEATHER");
	strcpy(myClocker->tableOfScreens[0][1], "%s");
	strcpy(myClocker->tableOfScreens[1][0], "TEMPERATURE");
	strcpy(myClocker->tableOfScreens[1][1], "%s");
	strcpy(myClocker->tableOfScreens[2][0], "FEELS LIKE");
	strcpy(myClocker->tableOfScreens[2][1], "%s");
	strcpy(myClocker->tableOfScreens[3][0], "PRESSURE");
	strcpy(myClocker->tableOfScreens[3][1], "%s");
	strcpy(myClocker->tableOfScreens[4][0], "HUMIDITY");
	strcpy(myClocker->tableOfScreens[4][1], "%s");
	strcpy(myClocker->tableOfScreens[5][0], "WIND SPEED");
	strcpy(myClocker->tableOfScreens[5][1], "%s");
	strcpy(myClocker->tableOfScreens[6][0], "SUNRISE");
	strcpy(myClocker->tableOfScreens[6][1], "%s");
	strcpy(myClocker->tableOfScreens[7][0], "SUNSET");
	strcpy(myClocker->tableOfScreens[7][1], "%s");
	strcpy(myClocker->tableOfScreens[8][0], "CITY");
	strcpy(myClocker->tableOfScreens[8][1], "%s");
	for(int i=0; i<9; i++)
	{
		strcpy(myClocker->contentOfScreens[i], "");
	}
}

void Clocker_Set_Time(Clocker * myClocker, uint8_t newHours, uint8_t newMinutes, uint8_t newSeconds)
{
	myClocker->sTime->Hours = newHours;
	myClocker->sTime->Minutes = newMinutes;
	myClocker->sTime->Seconds = newSeconds;
	myClocker->sTime->DayLightSaving = RTC_DAYLIGHTSAVING_NONE;
	myClocker->sTime->StoreOperation = RTC_STOREOPERATION_RESET;
	HAL_RTC_SetTime(myClocker->rtcHandle, myClocker->sTime, RTC_FORMAT_BIN);
}

void Clocker_Set_Alarm(Clocker * myClocker, uint8_t alarmHours, uint8_t alarmMinutes)
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

void Clocker_Segment_Update(Clocker * myClocker)
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

void Clocker_Change_Screen(Clocker * myClocker)
{
	if(myClocker->screenTimer >= myClocker->screenTimeChanging)
	{
		myClocker->screenTimer = 0;
		myClocker->currentScreen++;
		if(myClocker->currentScreen >= 10)
		{
			myClocker->currentScreen = 0;
		}
		lcd_clear();
		lcd_put_cur(0, 0);
		lcd_send_string(myClocker->tableOfScreens[myClocker->currentScreen][0]);
		lcd_put_cur(1, 0);
		lcd_send_string(myClocker->contentOfScreens[myClocker->currentScreen]);
	}
}

void Clocker_Alarm_Update(Clocker * myClocker)
{
	if(myClocker->alarmTimer >= 60)
	{
		myClocker->alarmTimer = 0;
		myClocker->alarm = 0;
		myClocker->alarmTimerOn = 0;
		DF_Pause();
	}
}

void Clocker_Run_Alarm(Clocker * myClocker, RTC_HandleTypeDef * hrtc)
{

	if(myClocker->alarm == 1)
	{
		myClocker->alarm = 0;
		myClocker->alarmTimerOn = 1;
		HAL_RTC_DeactivateAlarm(hrtc, RTC_ALARM_A);
		DF_PlayFromStart();
	}
}

void Clocker_Update_Timers(Clocker * myClocker)
{
	if(myClocker->alarmTimerOn == 1)
	{
		myClocker->alarmTimer++;
	}
	myClocker->screenTimer++;
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

void Clocker_Bluetooth(Clocker * myClocker, char * table)
{
	const char * result = strchr(table, '_');
			if (result != NULL)
			{
				table[result - table] = '\0';
			}
			cJSON * myTempObj = cJSON_Parse(table);
			if(cJSON_IsTrue(cJSON_GetObjectItem(myTempObj, "wifi")))
			{
				myClocker->wifi = 1;
				if(cJSON_IsTrue(cJSON_GetObjectItem(myTempObj, "weather")))
				{
					myClocker->weather = 1;
				}
				else
				{
					myClocker->weather = 0;
				}
			}
			else
			{
				myClocker->wifi = 0;
				if(cJSON_IsFalse(cJSON_GetObjectItem(myTempObj, "dateTime")))
				{
					myClocker->dateTime = 0;
					if (cJSON_IsNumber(cJSON_GetObjectItem(myTempObj, "dateTimeHours")) && cJSON_IsNumber(cJSON_GetObjectItem(myTempObj, "dateTimeMinutes")))
					{
						Clocker_Set_Time(myClocker, cJSON_GetObjectItem(myTempObj, "dateTimeHours")->valueint, cJSON_GetObjectItem(myTempObj, "dateTimeMinutes")->valueint, 0);
					}
				}
				else
				{
					myClocker->dateTime = 1;
				}
			}

			if(cJSON_IsTrue(cJSON_GetObjectItem(myTempObj, "alarm")))
			{
				Clocker_Set_Alarm(myClocker, cJSON_GetObjectItem(myTempObj, "alarmHours")->valueint, cJSON_GetObjectItem(myTempObj, "alarmMinutes")->valueint);
			}
			cJSON_Delete(myTempObj);
}

void Clocker_Wifi_Receive(Clocker * myClocker, char * table)
{
	const char * result = strchr(table, '_');
	cJSON * myTempObj = cJSON_Parse(table);
	strcpy(myClocker->contentOfScreens[0], cJSON_GetObjectItem(myTempObj, "weather")->valuestring);
	strcpy(myClocker->contentOfScreens[1], cJSON_GetObjectItem(myTempObj, "temp")->valuestring);
	strcpy(myClocker->contentOfScreens[2], cJSON_GetObjectItem(myTempObj, "feels_like")->valuestring);
	strcpy(myClocker->contentOfScreens[3], cJSON_GetObjectItem(myTempObj, "pressure")->valuestring);
	strcpy(myClocker->contentOfScreens[4], cJSON_GetObjectItem(myTempObj, "humidity")->valuestring);
	strcpy(myClocker->contentOfScreens[5], cJSON_GetObjectItem(myTempObj, "windspeed")->valuestring);
	strcpy(myClocker->contentOfScreens[6], cJSON_GetObjectItem(myTempObj, "sunrise")->valuestring);
	strcpy(myClocker->contentOfScreens[7], cJSON_GetObjectItem(myTempObj, "sunset")->valuestring);
	strcpy(myClocker->contentOfScreens[8], cJSON_GetObjectItem(myTempObj, "city")->valuestring);
	Clocker_Set_Time(&myClocker, atoi(cJSON_GetObjectItem(myTempObj, "hour")->valuestring), atoi(cJSON_GetObjectItem(myTempObj, "minutes")->valuestring), atoi(cJSON_GetObjectItem(myTempObj, "seconds")->valuestring));
	cJSON_Delete(myTempObj);
}
