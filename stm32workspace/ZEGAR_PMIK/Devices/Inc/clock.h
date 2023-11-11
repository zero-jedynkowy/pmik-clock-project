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
};



#endif /* INC_CLOCK_H_ */



//void updateTime(Clock *);
//uint8_t * getHour(Clock *);
//uint8_t * getMinutes(Clock *);