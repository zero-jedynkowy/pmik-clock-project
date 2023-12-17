/*
 * clock_rtc.h
 *
 *  Created on: Dec 2, 2023
 *      Author: xenon
 */

#ifndef INC_CLOCK_RTC_H_
#define INC_CLOCK_RTC_H_

#include "main.h"
#include "stm32l0xx_hal_rtc.h"
#include <stdint.h>

void setTime_RTC(RTC_TimeTypeDef *Time, uint8_t Godziny, uint8_t Minuty);
void updateTime_RTC(RTC_TimeTypeDef *Time);
uint8_t int2inta(uint8_t c);

#endif /* INC_CLOCK_RTC_H_ */