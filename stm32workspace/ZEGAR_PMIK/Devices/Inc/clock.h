/*
 * clock.h
 *
 *  Created on: Nov 11, 2023
 *      Author: zero-jedynkowy
 */

#include <stdint.h>

#ifndef INC_CLOCK_H_
#define INC_CLOCK_H_

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
	uint8_t timeToShow[4];
};

void setTime(struct Clock * myClock, uint8_t newHour, uint8_t newMinutes);
void updateTime(struct Clock *myClock);
uint8_t int2int(uint8_t c);

#endif /* INC_CLOCK_H_ */
