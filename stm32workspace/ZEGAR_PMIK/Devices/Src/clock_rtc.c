/*
 * clock_rtc.c
 *
 *  Created on: Dec 2, 2023
 *      Author: xenon
 */
#include <stdint.h>
#include <stdio.h>
#include "clock_rtc.h"
#include "TM1637.h"

void setTime_RTC(RTC_TimeTypeDef *Time, uint8_t Godziny, uint8_t Minuty)
{
  (*Time).Hours = Godziny;
  (*Time).Minutes = Minuty;
  printf("%d\n",(*Time).Minutes);
  (*Time).Seconds = 0x0;
  (*Time).DayLightSaving = RTC_DAYLIGHTSAVING_NONE;
  (*Time).StoreOperation = RTC_STOREOPERATION_RESET;
}

void updateTime_RTC(RTC_TimeTypeDef *Time)
{
	uint8_t total_time[4];
	total_time[0] = int2inta((*Time).Hours/10);
	total_time[1] = int2inta((*Time).Hours%10);
	total_time[2] = int2inta((*Time).Minutes/10);
	total_time[3] = int2inta((*Time).Minutes%10);
	printf("%d\n",(*Time).Minutes);
	tm1637_DisplayHandle(7, total_time);
}

uint8_t int2inta (uint8_t c)
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