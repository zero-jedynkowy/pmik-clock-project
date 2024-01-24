/*
 * Clocker.h
 *
 *  Created on: Dec 18, 2023
 *      Author: zero-jedynkowy
 */

#ifndef __CLOCKER_H
#define __CLOCKER_H

#include <stdint.h>
#include "DFPLAYER_MINI.h"
#include "TM1637.h"
#include "LCD1602.h"
#include "stm32l0xx_hal_rtc.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
	uint8_t screenTimer;
	uint8_t alarmTimer;
	uint8_t alarmTimerOn;
	uint8_t alarm;
	uint8_t wifi;
	uint8_t weather;
	uint8_t dateTime;
	char login[250];
	char password[250];
	char city[250];
	char contentOfScreens[10][16];
} typedef Clocker;

/** @brief Inicjalizuje strukturę zegara czyli godzinę,
 * 		   datę, pogodę oraz ekrany
 *
 *  @param Wykorzystujesz tutaj swoją strukturę Clocker która będzie zainicjalizowana
 *
 *  @param Przekazujesz Handler od RTC
 *
 *  @param Przekazujesz Handler od Timera 1
 *
 *  @param Przekazujesz Handler od Timer 2
 *
 *  @return Void.
 */
void Clocker_Init(Clocker * myClocker, RTC_HandleTypeDef * rtcHandle, TIM_HandleTypeDef * timSegment, TIM_HandleTypeDef * timScreen);

/** @brief Ustawienie czasu od którego zacznie odliczać czas.
 *
 *  @param Wykorzystujesz tutaj swoją strukturę Clocker która była już zainicjalizowana
 *
 *  @param Podajesz Godzinę którą chcesz ustawić w RTC
 *
 *  @param Podajesz Minutę którą chcesz ustawić w RTC
 *
 *  @param Podajesz Sekundę którą chcesz ustawić w RTC
 *
 *  @return Void.
 */
void Clocker_Set_Time(Clocker * myClocker, uint8_t newHours, uint8_t newMinutes, uint8_t newSeconds);

/** @brief Ustawienie czasu Alarmu w który ma się aktywować.
 *
 *  @param Wykorzystujesz tutaj swoją strukturę Clocker która była już zainicjalizowana
 *
 *  @param Ustawienie godziny alarmu
 *
 *  @param Ustawienie minut alarmu
 *
 *  @return Void.
 */
void Clocker_Set_Alarm(Clocker * myClocker, uint8_t alarmHours, uint8_t alarmMinutes);

/** @brief Przekonwertowanie liczby Intiger na znak Char
 *
 *  @param liczba typu Intiger
 *
 *  @return Void.
 */
uint8_t Clocker_Convert_Int_to_Segment(uint8_t c);

/** @brief Zaktualizowanie ekranu Segmentowego.
 *
 *  @param Wykorzystuje gotową strukturę typu Clocker
 *
 *  @return Void.
 */
void Clocker_Segment_Update(Clocker * myClocker);

/** @brief Zaktualizowanie ekranu LCD
 *
 *  @param Wykorzystuje gotową strukturę typu Clocker
 *
 *  @return Void.
 */
void Clocker_Change_Screen(Clocker * myClocker);

/** @brief Wpisywanie odgórnych nagłówków które LCD ma wyświetlić do struktury Clocker
 *
 *  @param Wykorzystuje gotową strukturę typu Clocker
 *
 *  @return Void.
 */
void Clocker_Set_Screens(Clocker * myClocker);


#endif
