/*
 * clock.c
 *
 *  Created on: Nov 11, 2023
 *      Author: zero-jedynkowy
 */
#include <stdint.h>

enum CLOCK_DEFINES
{
	HOUR_IN_SECONDS = 60*60,
	MINUTE_IN_SECONDS = 60
};

struct Clock
{
	uint32_t allTimeInSeconds ;
	uint8_t hour;
	uint8_t minutes;
};



//void setTime(struct Clock myClock)
//{
//
////	(*myClock).hour = newHour;
////	(*myClock).minutes = newMinutes;
////	(*myClock).allTimeInSeconds = newHour*HOUR_IN_SECONDS;
////	(*myClock).allTimeInSeconds += MINUTE_IN_SECONDS*newMinutes;
//}
//
//void updateTime(Clock *myClock)
//{
//	(*myClock).allTimeInSeconds++;
//	(*myClock).hour = (*myClock).allTimeInSeconds / HOUR_IN_SECONDS;
//	(*myClock).minutes = ((*myClock).allTimeInSeconds / MINUTE_IN_SECONDS) % MINUTE_IN_SECONDS;
//}
//
//uint8_t * getHour(Clock * myClock)
//{
//	return &(*myClock).hour;
//}
//
//uint8_t * getMinutes(Clock * myClock)
//{
//	return &(*myClock).minutes;
//}